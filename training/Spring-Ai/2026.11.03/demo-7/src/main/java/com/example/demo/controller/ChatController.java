package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ChatRequest;
import com.example.demo.dto.Conversation;
import com.example.demo.dto.ConversationSummary;
import com.example.demo.dto.ModelInfo;
import com.example.demo.service.ConversationService;
import com.example.demo.service.OllamaChatService;

@RestController //React 화면에 JSON으로 응답
@RequestMapping("/api")
public class ChatController {

	@Autowired
	private OllamaChatService ollamaChatService;

	@Autowired
	private ConversationService conversationService;

	//선택 가능한 모델 목록
	@GetMapping("/models")
	public List<ModelInfo> models() {
		return ollamaChatService.models();
	}

	//요청 : { "conversationId": null 또는 id, "question": "...", "model": "gemma3:latest" }
	//응답 : 질문/답변이 추가된 대화 전체
	@PostMapping("/chat")
	public Conversation chat(@RequestBody ChatRequest request) {
		return conversationService.chat(request.conversationId(), request.question(), request.model());
	}

	//대화 기록 목록
	@GetMapping("/conversations")
	public List<ConversationSummary> conversations() {
		return conversationService.list();
	}

	//대화 하나 불러오기
	@GetMapping("/conversations/{id}")
	public Conversation conversation(@PathVariable("id") String id) {
		return conversationService.get(id);
	}

	@DeleteMapping("/conversations/{id}")
	public void delete(@PathVariable("id") String id) {
		conversationService.delete(id);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> notFound(IllegalArgumentException e) {
		return Map.of("message", e.getMessage());
	}
}
