package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/** OCR 원문에서 합계, 날짜, 사업자번호를 정규식으로 찾는다. */
@Component
public class ReceiptParser {

	private static final Pattern DATE = Pattern
			.compile("(20\\d{2})\\s*[.\\-/년]\\s*(\\d{1,2})\\s*[.\\-/월]\\s*(\\d{1,2})");
	private static final Pattern BUSINESS_NUMBER = Pattern
			.compile("(?<!\\d)(\\d{3})\\s*-\\s*(\\d{2})\\s*-\\s*(\\d{5})(?!\\d)");
	private static final Pattern AMOUNT = Pattern.compile("(?<![\\d-])(\\d{1,3}(?:[,.]\\d{3})+|\\d{4,9})(?![\\d-])");
	private static final Pattern BROKEN_THOUSANDS = Pattern.compile("(\\d)[,.]\\s+(\\d{3})(?!\\d)");

	private static final int NEXT_LINES = 3;

	/** 앞쪽 그룹일수록 고객이 실제로 낸 금액에 가깝다. */
	private static final List<Pattern> TOTAL_KEYWORDS = List.of(
			keywords("환자부담총액", "납부금액", "수납금액", "결제금액", "받을금액", "청구금액"),
			keywords("총합계", "합계금액", "총금액", "총액", "합계", "TOTAL"));

	private static Pattern keywords(String... words) {
		List<String> spaced = new ArrayList<>();
		for (String word : words) {
			// OCR이 "합 계"처럼 글자 사이를 띄우는 경우까지 찾는다
			spaced.add(String.join("\\s*", word.split("")));
		}
		return Pattern.compile(String.join("|", spaced), Pattern.CASE_INSENSITIVE);
	}

	public String findDate(String text) {
		Matcher m = DATE.matcher(text);
		if (!m.find()) {
			return null;
		}
		return "%s-%02d-%02d".formatted(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
	}

	public String findBusinessNumber(String text) {
		Matcher m = BUSINESS_NUMBER.matcher(text);
		return m.find() ? m.group(1) + "-" + m.group(2) + "-" + m.group(3) : null;
	}

	/** 날짜와 사업자번호를 금액으로 착각하지 않도록 지운 뒤 금액을 모두 찾는다. */
	public List<Long> findAmounts(String text) {
		return amountsIn(clean(text));
	}

	public Long findTotal(String text) {
		String[] lines = clean(text).split("\\R");
		for (Pattern keyword : TOTAL_KEYWORDS) {
			for (int i = lines.length - 1; i >= 0; i--) {
				Matcher m = keyword.matcher(lines[i]);
				int end = -1;
				while (m.find()) {
					end = m.end();
				}
				if (end < 0) {
					continue;
				}
				List<Long> sameLine = amountsIn(lines[i].substring(end));
				if (!sameLine.isEmpty()) {
					return sameLine.get(sameLine.size() - 1);
				}
				// 표에서는 칸 이름 아래에 계산식 등이 끼어 있어 몇 줄 뒤까지 찾는다
				for (int j = i + 1; j < lines.length && j <= i + NEXT_LINES; j++) {
					List<Long> nextLine = amountsIn(lines[j]);
					if (!nextLine.isEmpty()) {
						return nextLine.get(0);
					}
				}
			}
		}
		return null;
	}

	private String clean(String text) {
		String cleaned = DATE.matcher(text).replaceAll(" ");
		cleaned = BUSINESS_NUMBER.matcher(cleaned).replaceAll(" ");
		return BROKEN_THOUSANDS.matcher(cleaned).replaceAll("$1,$2");
	}

	private List<Long> amountsIn(String text) {
		List<Long> amounts = new ArrayList<>();
		Matcher m = AMOUNT.matcher(text);
		while (m.find()) {
			amounts.add(Long.parseLong(m.group(1).replaceAll("[,.]", "")));
		}
		return amounts;
	}
}
