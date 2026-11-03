package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChatMessage;
import com.example.demo.dto.Conversation;
import com.example.demo.dto.ConversationSummary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

//대화 기록 저장 (JSON 파일에 저장해서 서버를 재시작해도 유지)
@Service
public class ConversationService {

	private final ObjectMapper objectMapper;
	private final OllamaChatService ollamaChatService;
	private final File file;
	private final Map<String, Conversation> store = new LinkedHashMap<>();

	public ConversationService(ObjectMapper objectMapper, OllamaChatService ollamaChatService,
			@Value("${chat.history.file:data/conversations.json}") String path) {
		this.objectMapper = objectMapper;
		this.ollamaChatService = ollamaChatService;
		this.file = new File(path);
	}

	//서버 시작 시 저장된 대화 불러오기
	@PostConstruct
	void load() throws IOException {
		if (!file.exists()) return;
		List<Conversation> list = objectMapper.readValue(file, new TypeReference<List<Conversation>>() {});
		list.forEach(c -> store.put(c.getId(), c));
	}

	private void save() {
		try {
			file.getAbsoluteFile().getParentFile().mkdirs();
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, new ArrayList<>(store.values()));
		} catch (IOException e) {
			throw new IllegalStateException("대화 기록 저장 실패", e);
		}
	}

	//최근 대화가 위로 오도록 정렬
	public synchronized List<ConversationSummary> list() {
		return store.values().stream()
				.sorted(Comparator.comparing(Conversation::getUpdatedAt).reversed())
				.map(ConversationSummary::of)
				.toList();
	}

	public synchronized Conversation get(String id) {
		Conversation c = store.get(id);
		if (c == null) throw new IllegalArgumentException("대화를 찾을 수 없습니다: " + id);
		return c;
	}

	public synchronized void delete(String id) {
		store.remove(id);
		save();
	}

	//질문 → 지난 대화와 함께 LLM 호출 → 질문/답변 기록
	public Conversation chat(String conversationId, String question, String model) {
		Conversation c;
		List<ChatMessage> history;
		synchronized (this) {
			c = conversationId == null ? create(question) : get(conversationId);
			history = List.copyOf(c.getMessages());
		}

		String answer;
		try {
			answer = ollamaChatService.chat(question, model, history);
		} catch (RuntimeException e) {
			//새 대화에서 첫 질문이 실패하면 빈 대화방은 지움
			synchronized (this) {
				if (c.getMessages().isEmpty()) store.remove(c.getId());
			}
			throw e;
		}

		synchronized (this) {
			String now = LocalDateTime.now().withNano(0).toString();
			c.getMessages().add(new ChatMessage("user", question, null, now));
			c.getMessages().add(new ChatMessage("assistant", answer, model, LocalDateTime.now().withNano(0).toString()));
			c.setModel(model);
			c.setUpdatedAt(now);
			save();
		}
		return c;
	}

	private Conversation create(String question) {
		Conversation c = new Conversation();
		c.setId(UUID.randomUUID().toString());
		c.setTitle(question.length() > 30 ? question.substring(0, 30) + "…" : question);
		c.setCreatedAt(LocalDateTime.now().withNano(0).toString());
		c.setUpdatedAt(c.getCreatedAt());
		store.put(c.getId(), c);
		return c;
	}
}
