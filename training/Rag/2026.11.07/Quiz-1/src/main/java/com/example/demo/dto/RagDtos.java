package com.example.demo.dto;

import java.util.List;

public final class RagDtos {

	private RagDtos() {
	}

	public record UploadResult(String fileName, int pages, int chunks) {
	}

	public record DocumentInfo(String fileName, int chunks, String uploadedAt) {
	}

	/** F08 출처 표시 */
	public record Source(String fileName, Integer page, double score, String snippet) {
	}

	public record SourcesEvent(List<Source> sources, boolean insufficient) {
	}

	public record TokenEvent(String text) {
	}

	public record ErrorEvent(String message) {
	}

	/** F10 평가용 질문. keywords가 비어 있으면 문서에 없는 질문이다. */
	public record EvalQuestion(String question, List<String> keywords) {
	}

	public record EvalRow(String question, boolean inScope, String topFile, Integer topPage, Double topScore,
			Integer firstHitRank) {
	}

	/** 설정 하나의 결과. inScope는 답이 있는 질문, outOfScope는 없는 질문 */
	public record EvalConfig(int topK, double threshold, int hits, int inScope, int rejected, int outOfScope,
			double accuracy) {
	}

	public record EvalReport(int questions, List<EvalConfig> configs, List<EvalRow> rows) {
	}
}
