package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OllamaChatService {

	private final ChatClient chatClient;
	public OllamaChatService(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	public String getQuestion() {
		return chatClient.prompt("거북선을 만든 한국의 유명한 장군을 알려줘").call().content();
	}
}
