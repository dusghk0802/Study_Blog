package com.example.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.Assistant;
import com.example.demo.dto.RagDtos.DocumentInfo;
import com.example.demo.dto.RagDtos.ErrorEvent;
import com.example.demo.dto.RagDtos.EvalReport;
import com.example.demo.dto.RagDtos.SourcesEvent;
import com.example.demo.dto.RagDtos.TokenEvent;
import com.example.demo.dto.RagDtos.UploadResult;
import com.example.demo.service.DocumentService;
import com.example.demo.service.EvaluationService;
import com.example.demo.service.RagService;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/api/{assistant}")
public class RagController {

	private static final Logger log = LoggerFactory.getLogger(RagController.class);

	private final DocumentService documentService;
	private final RagService ragService;
	private final EvaluationService evaluationService;
	private final int defaultTopK;
	private final double defaultThreshold;

	public RagController(DocumentService documentService, RagService ragService, EvaluationService evaluationService,
			@Value("${rag.default-top-k}") int defaultTopK,
			@Value("${rag.default-threshold}") double defaultThreshold) {
		this.documentService = documentService;
		this.ragService = ragService;
		this.evaluationService = evaluationService;
		this.defaultTopK = defaultTopK;
		this.defaultThreshold = defaultThreshold;
	}

	@PostMapping("/documents")
	public UploadResult upload(@PathVariable("assistant") Assistant assistant, @RequestParam("file") MultipartFile file)
			throws IOException {
		return documentService.upload(assistant, file);
	}

	@GetMapping("/documents")
	public List<DocumentInfo> documents(@PathVariable("assistant") Assistant assistant) {
		return documentService.list(assistant);
	}

	@DeleteMapping("/documents")
	public void delete(@PathVariable("assistant") Assistant assistant, @RequestParam("fileName") String fileName) {
		documentService.delete(assistant, fileName);
	}

	@GetMapping("/settings")
	public Map<String, Object> settings(@PathVariable("assistant") Assistant assistant) {
		return Map.of("title", assistant.title(), "topK", defaultTopK, "threshold", defaultThreshold);
	}

	/**
	 * 출처를 먼저 보내고 답변을 이어서 보낸다. 근거가 없으면(F09) LLM을 호출하지 않고 정해진 문구로 답한다.
	 * 토큰은 앞쪽 공백이 사라지지 않도록 JSON으로 보낸다.
	 */
	@GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<Object>> chat(@PathVariable("assistant") Assistant assistant,
			@RequestParam("question") String question,
			@RequestParam(value = "topK", required = false) Integer topK,
			@RequestParam(value = "threshold", required = false) Double threshold) {

		int k = topK == null ? defaultTopK : Math.clamp(topK, 1, 10);
		double t = threshold == null ? defaultThreshold : Math.clamp(threshold, 0.0, 1.0);

		return Flux.defer(() -> {
			List<Document> documents = ragService.search(assistant, question, k, t);

			if (documents.isEmpty()) {
				return Flux.just(
						event("sources", new SourcesEvent(List.of(), true)),
						event("token", new TokenEvent(RagService.INSUFFICIENT_ANSWER)),
						event("done", Map.of()));
			}

			return Flux.concat(
					Flux.just(event("sources", new SourcesEvent(ragService.toSources(documents), false))),
					ragService.answer(assistant, question, documents).map(text -> event("token", new TokenEvent(text))),
					Flux.just(event("done", Map.of())));
		})
				.subscribeOn(Schedulers.boundedElastic())
				.onErrorResume(e -> {
					log.error("Chat failed", e);
					return Flux.just(event("failure", new ErrorEvent("답변을 만들지 못했습니다. 잠시 후 다시 시도해 주세요.")));
				});
	}

	@PostMapping("/evaluate")
	public EvalReport evaluate(@PathVariable("assistant") Assistant assistant) throws IOException {
		return evaluationService.evaluate(assistant);
	}

	private ServerSentEvent<Object> event(String name, Object data) {
		return ServerSentEvent.builder(data).event(name).build();
	}
}
