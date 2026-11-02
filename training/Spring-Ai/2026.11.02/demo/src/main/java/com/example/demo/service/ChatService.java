package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

	@Autowired
	private ChatModel chatmodel;
	public void run() {
		ChatClient chatClient = ChatClient.builder(chatmodel).build();
		
		String response = chatClient
				.prompt("유명한 명언 2개만 알려줘")
				.call()
				.content();
		System.out.println("결과 : " + response);
	}

}
