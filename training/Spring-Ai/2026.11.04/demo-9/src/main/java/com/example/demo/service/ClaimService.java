package com.example.demo.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.ClaimPolicy;
import com.example.demo.dto.ClaimDtos.Check;
import com.example.demo.dto.ClaimDtos.ClaimResult;
import com.example.demo.dto.ClaimDtos.Decision;
import com.example.demo.dto.ClaimDtos.Item;
import com.example.demo.dto.ClaimDtos.OcrResult;
import com.example.demo.dto.ClaimDtos.Payout;
import com.example.demo.dto.ClaimDtos.ReceiptType;
import com.example.demo.dto.ClaimDtos.Status;
import com.example.demo.dto.ClaimDtos.StepResult;
import com.example.demo.dto.ClaimDtos.VisionReceipt;

/**
 * 영수증 심사 6단계.
 * 1 파일 → 2 OCR → 3 Vision AI → 4 금액·날짜 규칙 → 5 OCR과 Vision AI 교차 검증 → 6 지급 심사
 */
@Service
public class ClaimService {

	private static final Logger log = LoggerFactory.getLogger(ClaimService.class);
	private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png");
	private static final int MIN_IMAGE_SIDE = 300;
	/** 진료비 계산서의 본인부담금 합계는 100원 미만을 버려서 적는다. */
	private static final long SUM_TOLERANCE = 100;

	private final ImageOrienter imageOrienter;
	private final OcrService ocrService;
	private final VisionService visionService;
	private final ClaimPolicy policy;

	public ClaimService(ImageOrienter imageOrienter, OcrService ocrService, VisionService visionService,
			ClaimPolicy policy) {
		this.imageOrienter = imageOrienter;
		this.ocrService = ocrService;
		this.visionService = visionService;
		this.policy = policy;
	}

	public ClaimResult analyze(MultipartFile file) {
		List<StepResult> steps = new ArrayList<>();

		// 1단계: 파일
		byte[] bytes = readBytes(file);
		BufferedImage image = decode(bytes);
		StepResult fileStep = checkFile(file, bytes, image);
		steps.add(fileStep);
		if (fileStep.status() == Status.FAIL) {
			skipFrom(2, steps);
			return new ClaimResult(Decision.REJECTED, "영수증 이미지를 읽을 수 없습니다. JPG나 PNG 파일로 다시 올려 주세요.",
					null, steps, null, null);
		}

		// 휴대폰 사진은 회전 정보를 따로 저장하므로 똑바로 세운 뒤 OCR과 Vision AI에 넘긴다
		ImageOrienter.Upright upright = imageOrienter.upright(bytes, image, file.getContentType());

		// 2단계: OCR
		OcrResult ocr = null;
		String ocrError = null;
		try {
			ocr = ocrService.read(upright.bytes(), upright.contentType());
		} catch (Exception e) {
			log.error("OCR failed", e);
			ocrError = "OCR을 실행하지 못했습니다.";
		}
		steps.add(checkOcr(ocr, ocrError));

		// 3단계: Vision AI
		VisionReceipt vision;
		try {
			vision = visionService.analyze(upright.contentType(), upright.bytes());
		} catch (Exception e) {
			log.error("Vision AI failed", e);
			steps.add(new StepResult(3, "Vision AI 분석", Status.FAIL, "Vision AI를 호출하지 못했습니다.", List.of()));
			skipFrom(4, steps);
			return new ClaimResult(Decision.REVIEW, "자동 분석을 끝내지 못했습니다. 담당자가 확인합니다.", null, steps, ocr, null);
		}
		StepResult visionStep = checkVision(vision);
		steps.add(visionStep);

		ReceiptType type = vision == null ? null : vision.receiptType();
		if (type == null || type == ReceiptType.NOT_RECEIPT) {
			skipFrom(4, steps);
			return new ClaimResult(Decision.REJECTED, "영수증으로 확인되지 않았습니다. 영수증 사진을 다시 올려 주세요.",
					null, steps, ocr, vision);
		}

		// 4단계: 금액·날짜 규칙
		steps.add(checkRules(vision));

		// 5단계: 교차 검증
		List<Check> crossChecks = crossCheck(ocr, vision);
		steps.add(new StepResult(5, "OCR·Vision AI 교차 검증", worst(crossChecks), crossSummary(crossChecks), crossChecks));

		// 6단계: 지급 심사
		return decide(steps, ocr, vision, crossChecks);
	}

	private byte[] readBytes(MultipartFile file) {
		try {
			return file.getBytes();
		} catch (IOException e) {
			throw new IllegalStateException("업로드한 파일을 읽지 못했습니다.", e);
		}
	}

	private BufferedImage decode(byte[] bytes) {
		try {
			return ImageIO.read(new ByteArrayInputStream(bytes));
		} catch (IOException e) {
			return null;
		}
	}

	private StepResult checkFile(MultipartFile file, byte[] bytes, BufferedImage image) {
		List<Check> checks = new ArrayList<>();
		boolean allowedType = file.getContentType() != null && ALLOWED_TYPES.contains(file.getContentType());
		checks.add(Check.of("파일 형식", allowedType ? Status.PASS : Status.FAIL,
				allowedType ? file.getContentType() : "JPG, PNG만 올릴 수 있습니다."));
		checks.add(Check.of("이미지 열기", image != null ? Status.PASS : Status.FAIL,
				image != null ? image.getWidth() + " × " + image.getHeight() + " px" : "이미지 파일이 아니거나 손상되었습니다."));
		if (image != null) {
			boolean largeEnough = Math.min(image.getWidth(), image.getHeight()) >= MIN_IMAGE_SIDE;
			checks.add(Check.of("해상도", largeEnough ? Status.PASS : Status.WARN,
					largeEnough ? "글자를 읽기에 충분합니다." : "이미지가 작아 글자를 잘못 읽을 수 있습니다."));
		}
		Status status = worst(checks);
		return new StepResult(1, "파일 검증", status,
				status == Status.FAIL ? "파일을 처리할 수 없습니다." : (bytes.length / 1024) + "KB 이미지를 확인했습니다.", checks);
	}

	private StepResult checkOcr(OcrResult ocr, String error) {
		if (ocr == null) {
			return new StepResult(2, "OCR 문자 인식", Status.WARN, error, List.of());
		}
		List<Check> checks = new ArrayList<>();
		Status confidence = ocr.confidence() >= policy.ocrPassConfidence() ? Status.PASS
				: ocr.confidence() >= policy.ocrWarnConfidence() ? Status.WARN : Status.FAIL;
		checks.add(Check.of("인식 신뢰도", confidence, ocr.confidence() + "%"));
		checks.add(found("합계 금액", ocr.totalAmount() == null ? null : won(ocr.totalAmount())));
		checks.add(found("날짜", ocr.visitDate()));
		checks.add(found("사업자번호", ocr.businessNumber()));
		Status status = worst(checks);
		return new StepResult(2, "OCR 문자 인식", status, "신뢰도 " + ocr.confidence() + "%로 글자를 읽었습니다.", checks);
	}

	private Check found(String name, String value) {
		return Check.of(name, value != null ? Status.PASS : Status.WARN, value != null ? value : "찾지 못했습니다.");
	}

	private StepResult checkVision(VisionReceipt vision) {
		if (vision == null || vision.receiptType() == null || vision.receiptType() == ReceiptType.NOT_RECEIPT) {
			return new StepResult(3, "Vision AI 분석", Status.FAIL, "영수증이 아닌 이미지로 판단했습니다.",
					List.of(Check.of("영수증 여부", Status.FAIL, "영수증이 아닙니다.")));
		}
		List<Check> checks = new ArrayList<>();
		boolean medical = isMedical(vision);
		checks.add(Check.of("영수증 종류", medical ? Status.PASS : Status.FAIL, typeLabel(vision.receiptType())));
		checks.add(found("상호", vision.merchantName()));
		checks.add(found("합계 금액", vision.totalAmount() == null ? null : won(vision.totalAmount())));
		checks.add(found("날짜", vision.visitDate()));
		checks.add(found("사업자번호", vision.businessNumber()));
		return new StepResult(3, "Vision AI 분석", worst(checks),
				medical ? "의료비 영수증으로 판단했습니다." : "의료비 영수증이 아닙니다.", checks);
	}

	private StepResult checkRules(VisionReceipt vision) {
		List<Check> checks = new ArrayList<>();

		Long sum = sumOfItems(vision);
		if (sum == null || vision.totalAmount() == null) {
			checks.add(Check.of("항목 합계", Status.WARN, "항목이나 합계가 없어 계산하지 못했습니다."));
		} else if (Math.abs(sum - vision.totalAmount()) < SUM_TOLERANCE) {
			checks.add(Check.of("항목 합계", Status.PASS,
					"항목 합계 " + won(sum) + "과 총액 " + won(vision.totalAmount()) + "이 맞습니다."));
		} else {
			checks.add(Check.of("항목 합계", Status.FAIL,
					"항목 합계 " + won(sum) + "과 총액 " + won(vision.totalAmount()) + "이 다릅니다."));
		}

		checks.add(checkBusinessNumber(vision.businessNumber()));
		checks.add(checkDateWindow(vision.visitDate()));

		Status status = worst(checks);
		return new StepResult(4, "금액·날짜 규칙 검증", status,
				status == Status.PASS ? "규칙 검증을 모두 통과했습니다." : "확인이 필요한 항목이 있습니다.", checks);
	}

	private Check checkBusinessNumber(String number) {
		if (number == null) {
			return Check.of("사업자번호 형식", Status.WARN, "사업자번호가 없습니다.");
		}
		return isValidBusinessNumber(number)
				? Check.of("사업자번호 형식", Status.PASS, "검증번호가 맞습니다.")
				: Check.of("사업자번호 형식", Status.WARN, "검증번호가 맞지 않습니다. 잘못 읽었거나 잘못된 번호일 수 있습니다.");
	}

	/** 국세청 사업자등록번호 검증번호 공식 */
	static boolean isValidBusinessNumber(String number) {
		String digits = number.replaceAll("\\D", "");
		if (digits.length() != 10) {
			return false;
		}
		int[] weights = { 1, 3, 7, 1, 3, 7, 1, 3, 5 };
		int sum = 0;
		for (int i = 0; i < 9; i++) {
			sum += (digits.charAt(i) - '0') * weights[i];
		}
		sum += (digits.charAt(8) - '0') * 5 / 10;
		return (10 - sum % 10) % 10 == digits.charAt(9) - '0';
	}

	private Check checkDateWindow(String date) {
		LocalDate parsed = parseDate(date);
		if (parsed == null) {
			return Check.of("청구 가능 기간", Status.WARN, "날짜를 확인하지 못했습니다.");
		}
		return isClaimable(parsed)
				? Check.of("청구 가능 기간", Status.PASS, date + "은 청구 기간 안입니다.")
				: Check.of("청구 가능 기간", Status.FAIL, date + "은 청구할 수 있는 기간이 아닙니다.");
	}

	private boolean isClaimable(LocalDate date) {
		LocalDate today = LocalDate.now();
		return !date.isAfter(today) && !date.isBefore(today.minusYears(policy.claimWindowYears()));
	}

	private List<Check> crossCheck(OcrResult ocr, VisionReceipt vision) {
		if (ocr == null) {
			return List.of(new Check("OCR 결과", null, null, Status.FAIL, "OCR 결과가 없어 비교하지 못했습니다."));
		}
		List<Check> checks = new ArrayList<>();
		checks.add(compareTotal(ocr, vision));
		checks.add(compareText("날짜", ocr.visitDate(), vision.visitDate()));
		checks.add(compareText("사업자번호", ocr.businessNumber(), vision.businessNumber()));
		checks.add(compareMerchant(ocr, vision));
		checks.add(compareItems(ocr, vision));
		return checks;
	}

	private Check compareTotal(OcrResult ocr, VisionReceipt vision) {
		Long o = ocr.totalAmount();
		Long v = vision.totalAmount();
		String ocrText = o == null ? null : won(o);
		String visionText = v == null ? null : won(v);
		if (v == null) {
			return new Check("합계 금액", ocrText, null, Status.FAIL, "Vision AI가 합계를 읽지 못했습니다.");
		}
		if (v.equals(o)) {
			return new Check("합계 금액", ocrText, visionText, Status.PASS, "일치합니다.");
		}
		if (ocr.amounts().contains(v)) {
			return new Check("합계 금액", ocrText, visionText, Status.WARN,
					"OCR 원문에 같은 금액은 있지만 합계로 확인되지는 않았습니다.");
		}
		return new Check("합계 금액", ocrText, visionText, Status.FAIL, "두 결과가 다릅니다.");
	}

	private Check compareText(String name, String ocrValue, String visionValue) {
		if (ocrValue == null || visionValue == null) {
			return new Check(name, ocrValue, visionValue, Status.WARN, "한쪽에서 찾지 못해 비교하지 못했습니다.");
		}
		boolean same = ocrValue.equals(visionValue);
		return new Check(name, ocrValue, visionValue, same ? Status.PASS : Status.FAIL,
				same ? "일치합니다." : "두 결과가 다릅니다.");
	}

	/** OCR 원문 안에서 상호와 가장 비슷한 구간을 찾는다. OCR은 글자 한두 개를 틀리는 일이 많다. */
	private Check compareMerchant(OcrResult ocr, VisionReceipt vision) {
		String name = normalize(vision.merchantName());
		if (name.isEmpty()) {
			return new Check("상호", null, null, Status.WARN, "Vision AI가 상호를 읽지 못했습니다.");
		}
		String text = normalize(ocr.rawText());
		double best = 0;
		for (int i = 0; i + name.length() <= text.length(); i++) {
			best = Math.max(best, similarity(name, text.substring(i, i + name.length())));
		}
		int percent = (int) Math.round(best * 100);
		Status status = best >= 0.7 ? Status.PASS : Status.WARN;
		return new Check("상호", "원문과 " + percent + "% 유사", vision.merchantName(), status,
				status == Status.PASS ? "OCR 원문에서 확인했습니다." : "OCR 원문에서 확인하지 못했습니다.");
	}

	private Check compareItems(OcrResult ocr, VisionReceipt vision) {
		List<Long> amounts = itemAmounts(vision);
		if (amounts.isEmpty()) {
			return new Check("항목 금액", null, null, Status.WARN, "Vision AI가 항목을 읽지 못했습니다.");
		}
		long matched = amounts.stream().filter(amount -> containsAmount(ocr.rawText(), amount)).count();
		String detail = matched + " / " + amounts.size() + "개";
		Status status = matched == amounts.size() ? Status.PASS : Status.WARN;
		return new Check("항목 금액", detail, amounts.size() + "개", status,
				status == Status.PASS ? "모든 항목 금액을 OCR 원문에서 확인했습니다." : "일부 금액을 OCR 원문에서 찾지 못했습니다.");
	}

	/** 996처럼 세 자리 금액도 찾을 수 있도록 원문에서 그 숫자를 직접 찾는다. */
	private boolean containsAmount(String text, long amount) {
		String plain = Long.toString(amount);
		String comma = NumberFormat.getNumberInstance(Locale.KOREA).format(amount);
		return Pattern.compile("(?<![\\d,])(" + plain + "|" + comma + ")(?![\\d,])").matcher(text).find();
	}

	private String crossSummary(List<Check> checks) {
		long passed = checks.stream().filter(c -> c.status() == Status.PASS).count();
		return "비교 항목 " + checks.size() + "개 중 " + passed + "개가 일치합니다.";
	}

	private ClaimResult decide(List<StepResult> steps, OcrResult ocr, VisionReceipt vision, List<Check> crossChecks) {
		Status totalCheck = statusOf(crossChecks, "합계 금액");
		Status dateCheck = statusOf(crossChecks, "날짜");
		LocalDate date = parseDate(vision.visitDate());
		boolean outOfWindow = dateCheck == Status.PASS && date != null && !isClaimable(date);

		Payout payout = calculatePayout(ocr, vision);
		List<Check> checks = new ArrayList<>();
		Decision decision;
		String message;

		if (!isMedical(vision)) {
			decision = Decision.REJECTED;
			message = "의료비 영수증이 아니어서 보험금을 지급할 수 없습니다.";
			payout = null;
			checks.add(Check.of("보장 대상", Status.FAIL, "병원, 약국 영수증만 청구할 수 있습니다."));
		} else if (outOfWindow) {
			decision = Decision.REJECTED;
			message = "청구할 수 있는 기간이 아니어서 보험금을 지급할 수 없습니다.";
			payout = null;
			checks.add(Check.of("청구 기간", Status.FAIL, vision.visitDate() + "은 청구 기간 밖입니다."));
		} else if (payout == null) {
			decision = Decision.REVIEW;
			message = "금액을 확인하지 못했습니다. 담당자가 영수증을 확인합니다.";
			checks.add(Check.of("지급액 계산", Status.WARN, "합계 금액이 없어 계산하지 못했습니다."));
		} else if (payout.baseAmount() <= 0) {
			decision = Decision.REJECTED;
			message = "보장되는 항목이 없어 지급할 금액이 없습니다.";
			checks.add(Check.of("보장 항목", Status.FAIL, "모든 항목이 보장에서 제외됩니다."));
		} else {
			boolean verified = totalCheck == Status.PASS && dateCheck == Status.PASS;
			boolean hasFailure = steps.stream().anyMatch(s -> s.status() == Status.FAIL);
			boolean smallEnough = payout.claimedAmount() <= policy.autoApproveLimit();
			checks.add(Check.of("합계·날짜 일치", verified ? Status.PASS : Status.WARN,
					verified ? "OCR과 Vision AI의 합계와 날짜가 같습니다." : "합계나 날짜가 확인되지 않았습니다."));
			checks.add(Check.of("이전 단계 실패", hasFailure ? Status.WARN : Status.PASS,
					hasFailure ? "실패한 검증 단계가 있습니다." : "실패한 단계가 없습니다."));
			checks.add(Check.of("자동 지급 한도", smallEnough ? Status.PASS : Status.WARN,
					smallEnough ? "자동 지급 가능 금액입니다." : won(policy.autoApproveLimit()) + "을 넘어 담당자 확인이 필요합니다."));
			if (verified && !hasFailure && smallEnough) {
				decision = Decision.APPROVED;
				message = "고객님께 지급될 금액은 " + won(payout.payoutAmount()) + "입니다.";
			} else {
				decision = Decision.REVIEW;
				message = "예상 지급액은 " + won(payout.payoutAmount()) + "이며, 담당자 확인 후 확정됩니다.";
			}
		}

		Status status = decision == Decision.APPROVED ? Status.PASS
				: decision == Decision.REVIEW ? Status.WARN : Status.FAIL;
		steps.add(new StepResult(6, "지급 심사", status, message, checks));
		return new ClaimResult(decision, message, payout, steps, ocr, vision);
	}

	/** 합계가 확인되지 않으면 안내 금액이 부풀려지지 않도록 Vision AI 합계, OCR 합계, 항목 합계 중 가장 작은 값을 쓴다. */
	private Payout calculatePayout(OcrResult ocr, VisionReceipt vision) {
		Long total = vision.totalAmount();
		if (total == null) {
			return null;
		}
		long claimed = total;
		// OCR 원문에 같은 금액이 있으면 두 결과가 합계를 확인해 준 것이므로 그 금액을 쓴다.
		// 항목은 빠뜨리고 읽는 경우가 있어, 확인된 합계를 항목 합계로 깎지 않는다(4단계에서 담당자 확인으로 넘어간다).
		boolean confirmedByOcr = ocr != null && ocr.amounts().contains(total);
		if (!confirmedByOcr) {
			if (ocr != null && ocr.totalAmount() != null) {
				claimed = Math.min(claimed, ocr.totalAmount());
			}
			Long itemSum = sumOfItems(vision);
			if (itemSum != null) {
				claimed = Math.min(claimed, itemSum);
			}
		}
		long excluded = vision.items() == null ? 0 : vision.items().stream()
				.filter(i -> i.amount() != null && policy.isExcluded(i.name()))
				.mapToLong(Item::amount)
				.sum();
		long base = Math.max(claimed - excluded, 0);
		long deductible = Math.max(policy.minimumDeductible(), base * policy.deductiblePercent() / 100);
		long payout = Math.min(Math.max(base - deductible, 0), policy.payoutLimit());
		return new Payout(claimed, excluded, base, deductible, payout / 10 * 10);
	}

	private void skipFrom(int order, List<StepResult> steps) {
		List<String> titles = List.of("파일 검증", "OCR 문자 인식", "Vision AI 분석", "금액·날짜 규칙 검증", "OCR·Vision AI 교차 검증", "지급 심사");
		for (int i = order; i <= titles.size(); i++) {
			steps.add(new StepResult(i, titles.get(i - 1), Status.SKIPPED, "이전 단계 결과로 진행하지 않았습니다.", List.of()));
		}
	}

	private boolean isMedical(VisionReceipt vision) {
		return vision.receiptType() == ReceiptType.HOSPITAL || vision.receiptType() == ReceiptType.PHARMACY;
	}

	private String typeLabel(ReceiptType type) {
		return switch (type) {
			case HOSPITAL -> "병원 영수증";
			case PHARMACY -> "약국 영수증";
			case OTHER -> "의료비가 아닌 영수증";
			case NOT_RECEIPT -> "영수증 아님";
		};
	}

	private Long sumOfItems(VisionReceipt vision) {
		List<Long> amounts = itemAmounts(vision);
		return amounts.isEmpty() ? null : amounts.stream().mapToLong(Long::longValue).sum();
	}

	private List<Long> itemAmounts(VisionReceipt vision) {
		return vision.items() == null ? List.of()
				: vision.items().stream().map(Item::amount).filter(Objects::nonNull).toList();
	}

	private Status statusOf(List<Check> checks, String name) {
		return checks.stream().filter(c -> c.name().equals(name)).map(Check::status).findFirst().orElse(Status.FAIL);
	}

	private Status worst(List<Check> checks) {
		if (checks.stream().anyMatch(c -> c.status() == Status.FAIL)) {
			return Status.FAIL;
		}
		return checks.stream().anyMatch(c -> c.status() == Status.WARN) ? Status.WARN : Status.PASS;
	}

	private LocalDate parseDate(String date) {
		if (date == null) {
			return null;
		}
		try {
			return LocalDate.parse(date);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	private String normalize(String text) {
		if (text == null) {
			return "";
		}
		return text.replace("주식회사", "").replace("(주)", "").replaceAll("\\s+", "");
	}

	private double similarity(String a, String b) {
		int[] previous = new int[b.length() + 1];
		for (int j = 0; j <= b.length(); j++) {
			previous[j] = j;
		}
		for (int i = 1; i <= a.length(); i++) {
			int[] current = new int[b.length() + 1];
			current[0] = i;
			for (int j = 1; j <= b.length(); j++) {
				int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
				current[j] = Math.min(Math.min(current[j - 1] + 1, previous[j] + 1), previous[j - 1] + cost);
			}
			previous = current;
		}
		return 1.0 - (double) previous[b.length()] / Math.max(a.length(), 1);
	}

	private String won(long amount) {
		return NumberFormat.getNumberInstance(Locale.KOREA).format(amount) + "원";
	}
}
