package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.example.demo.config.Assistant;
import com.example.demo.dto.RagDtos.EvalConfig;
import com.example.demo.dto.RagDtos.EvalQuestion;
import com.example.demo.dto.RagDtos.EvalReport;
import com.example.demo.dto.RagDtos.EvalRow;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * F10 검색 품질 평가. 질문 10개를 한 번씩만 검색하고(임베딩 10회) 그 결과로 설정별 점수를 계산한다.
 * 답이 있는 질문은 정답 키워드가 들어 있는 청크가 검색되면, 답이 없는 질문은 아무것도 검색되지 않으면 맞은 것이다.
 */
@Service
public class EvaluationService {

	private static final int[] TOP_KS = { 1, 3, 5 };
	private static final double[] THRESHOLDS = { 0.0, 0.3, 0.4, 0.5 };
	private static final int MAX_TOP_K = 5;

	private final RagService ragService;
	private final ObjectMapper objectMapper;

	public EvaluationService(RagService ragService, ObjectMapper objectMapper) {
		this.ragService = ragService;
		this.objectMapper = objectMapper;
	}

	public EvalReport evaluate(Assistant assistant) throws IOException {
		List<EvalQuestion> questions = load(assistant);

		List<List<Document>> results = new ArrayList<>();
		List<EvalRow> rows = new ArrayList<>();
		for (EvalQuestion question : questions) {
			List<Document> found = ragService.search(assistant, question.question(), MAX_TOP_K, 0.0);
			results.add(found);
			rows.add(toRow(question, found));
		}

		List<EvalConfig> configs = new ArrayList<>();
		for (int topK : TOP_KS) {
			for (double threshold : THRESHOLDS) {
				configs.add(score(questions, results, topK, threshold));
			}
		}
		return new EvalReport(questions.size(), configs, rows);
	}

	private EvalConfig score(List<EvalQuestion> questions, List<List<Document>> results, int topK, double threshold) {
		int hits = 0;
		int inScope = 0;
		int rejected = 0;
		int outOfScope = 0;

		for (int i = 0; i < questions.size(); i++) {
			EvalQuestion question = questions.get(i);
			List<Document> kept = results.get(i).stream()
					.filter(document -> document.getScore() != null && document.getScore() >= threshold)
					.limit(topK)
					.toList();

			if (isInScope(question)) {
				inScope++;
				if (firstHit(question, kept) != null) {
					hits++;
				}
			} else {
				outOfScope++;
				if (kept.isEmpty()) {
					rejected++;
				}
			}
		}
		double accuracy = questions.isEmpty() ? 0 : (double) (hits + rejected) / questions.size();
		return new EvalConfig(topK, threshold, hits, inScope, rejected, outOfScope, accuracy);
	}

	private EvalRow toRow(EvalQuestion question, List<Document> found) {
		Document top = found.isEmpty() ? null : found.get(0);
		Object page = top == null ? null : top.getMetadata().get("page_number");
		return new EvalRow(
				question.question(),
				isInScope(question),
				top == null ? null : String.valueOf(top.getMetadata().get("file_name")),
				page instanceof Number number ? number.intValue() : null,
				top == null ? null : top.getScore(),
				isInScope(question) ? firstHit(question, found) : null);
	}

	/** 정답 키워드가 처음 나온 순위(1부터). 없으면 null */
	private Integer firstHit(EvalQuestion question, List<Document> documents) {
		for (int i = 0; i < documents.size(); i++) {
			String text = normalize(documents.get(i).getText());
			if (question.keywords().stream().anyMatch(keyword -> text.contains(normalize(keyword)))) {
				return i + 1;
			}
		}
		return null;
	}

	private boolean isInScope(EvalQuestion question) {
		return question.keywords() != null && !question.keywords().isEmpty();
	}

	private String normalize(String text) {
		return text == null ? "" : text.replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
	}

	private List<EvalQuestion> load(Assistant assistant) throws IOException {
		ClassPathResource resource = new ClassPathResource("eval/" + assistant.name() + ".json");
		try (InputStream in = resource.getInputStream()) {
			return objectMapper.readValue(in, new TypeReference<List<EvalQuestion>>() {
			});
		}
	}
}
