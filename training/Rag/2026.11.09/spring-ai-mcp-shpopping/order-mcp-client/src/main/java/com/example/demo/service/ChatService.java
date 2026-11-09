package com.example.demo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	
	private final ChatClient chatClient;
	
	public ChatService(ChatClient chatClient) {
		this.chatClient = chatClient; //MCP 정보가 포함된 환경정보를 가지고 있는
	}
	
	public String chat(String question) {
		return chatClient
				.prompt()
				.system("""
	                    당신은 쇼핑몰 주문 조회 AI 상담원입니다.

	                    주문번호가 포함된 조회 요청이면
	                    getOrder 도구를 사용하세요.

	                    도구에서 반환한 정보만 답변하세요.
	                    없는 주문 정보는 만들지 마세요.
	                    주문번호가 없다면 번호를 질문하세요.
	                    한국어로 답변하세요.
	                    """)
				.user(question)
				.call()
				.content();
	}

}


