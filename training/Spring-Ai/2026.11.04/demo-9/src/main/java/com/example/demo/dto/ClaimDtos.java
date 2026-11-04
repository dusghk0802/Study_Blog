package com.example.demo.dto;

import java.util.List;

/** 분석 과정에서 주고받는 데이터 */
public final class ClaimDtos {

	private ClaimDtos() {
	}

	public enum Status { PASS, WARN, FAIL, SKIPPED }

	public enum Decision { APPROVED, REVIEW, REJECTED }

	public enum ReceiptType { HOSPITAL, PHARMACY, OTHER, NOT_RECEIPT }

	/** Vision AI가 이미지에서 읽은 영수증 정보 */
	public record VisionReceipt(
			ReceiptType receiptType,
			String merchantName,
			String businessNumber,
			String visitDate,
			List<Item> items,
			Long totalAmount) {
	}

	public record Item(String name, Long amount, Long insurerAmount) {
	}

	/** OCR 결과 */
	public record OcrResult(
			String rawText,
			int confidence,
			Long totalAmount,
			String visitDate,
			String businessNumber,
			List<Long> amounts) {
	}

	/** 개별 검증 항목. 교차 검증에서는 ocrValue와 visionValue를 함께 보여준다. */
	public record Check(String name, String ocrValue, String visionValue, Status status, String message) {

		public static Check of(String name, Status status, String message) {
			return new Check(name, null, null, status, message);
		}
	}

	public record StepResult(int order, String title, Status status, String summary, List<Check> checks) {
	}

	public record Payout(long claimedAmount, long excludedAmount, long baseAmount, long deductible, long payoutAmount) {
	}

	public record ClaimResult(
			Decision decision,
			String message,
			Payout payout,
			List<StepResult> steps,
			OcrResult ocr,
			VisionReceipt vision) {
	}
}
