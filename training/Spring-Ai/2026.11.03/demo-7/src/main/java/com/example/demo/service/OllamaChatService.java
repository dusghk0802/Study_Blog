package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChatMessage;
import com.example.demo.dto.ModelInfo;

@Service
public class OllamaChatService {

	private final ChatClient chatClient;
	private final OllamaApi ollamaApi;

	public OllamaChatService(ChatClient.Builder builder, OllamaApi ollamaApi) {
		this.chatClient = builder.build();
		this.ollamaApi = ollamaApi;
	}

	public String getQuestion() {
		return chatClient.prompt("거북선을 만든 한국의 유명한 장군을 알려줘").call().content();
	}

	//지난 대화 + 새 질문을 선택한 모델로 LLM 호출
	public String chat(String question, String model, List<ChatMessage> history) {
		//지난 대화를 Spring AI Message로 변환
		List<Message> messages = new ArrayList<>();
		for (ChatMessage m : history) {
			if ("user".equals(m.role())) {
				messages.add(new UserMessage(m.text()));
			} else {
				messages.add(new AssistantMessage(m.text()));
			}
		}

		return chatClient.prompt()
				         .system("""
				         		당신은 정확성을 가장 중요하게 여기는 AI 비서입니다.
				         		한국어로 답변하세요.
				         		확실하지 않은 사실은 지어내지 말고 모른다고 답하세요.
				         		지난 대화 내용을 참고해서 답변하세요.
				         		""")
				         .messages(messages) //이전 대화 내용
				         .user(question) //사용자 메시지
				         .options(OllamaChatOptions.builder().model(model).build()) //화면에서 선택한 모델
				         .call()
				         .content();
	}

	//Ollama에 설치된 채팅 모델 목록 (임베딩 모델은 제외)
	public List<ModelInfo> models() {
		return ollamaApi.listModels().models().stream()
				.filter(m -> !m.name().contains("embed"))
				.map(m -> new ModelInfo(
						m.name(),
						m.details() != null ? m.details().parameterSize() : null,
						m.size()))
				.toList();
	}
}
