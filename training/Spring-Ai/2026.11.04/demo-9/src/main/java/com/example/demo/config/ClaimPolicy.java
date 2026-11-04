package com.example.demo.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** 보험금 심사 기준. 실습용 예시 값이며 실제 약관이 아니다. */
@ConfigurationProperties("claim")
public record ClaimPolicy(
		long minimumDeductible,
		int deductiblePercent,
		long payoutLimit,
		long autoApproveLimit,
		int claimWindowYears,
		int ocrPassConfidence,
		int ocrWarnConfidence,
		List<String> excludedKeywords) {

	public ClaimPolicy {
		if (excludedKeywords == null || excludedKeywords.isEmpty()) {
			excludedKeywords = List.of("제증명", "진단서", "소견서", "주차", "식대", "간병", "미용", "성형");
		}
	}

	public boolean isExcluded(String itemName) {
		return itemName != null && excludedKeywords.stream().anyMatch(itemName::contains);
	}
}
