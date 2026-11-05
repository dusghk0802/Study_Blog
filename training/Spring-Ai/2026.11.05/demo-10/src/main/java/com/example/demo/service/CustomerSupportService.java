package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ChatMessageDto;
import com.example.demo.dto.ChatRequestDto;
import com.example.demo.dto.ChatResponseDto;
import com.example.demo.entity.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerSupportService {

	@Autowired
	private ChatMessageRepository chatMessageRepository;
	
	private final ChatClient chatClient;
	
	public CustomerSupportService(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	
	private final Map<String, List<Message>> chatHistory =
			new ConcurrentHashMap<String, List<Message>>();
	
	private final String systemPrompt = """
			당신은 "Awesome Tech"라는 회사의 E-커머스 고객 지원 챗봇입니다.
			항상 친절하고 명확하게 답변해야 합니다. 사용자가 상품에 대해서 물으면 아래 정보를 기반으로 답해주세요.
			
			- 상품명: 갤럭시 AI 북
			- 가격: 1,500,000원
			- 특징: 최신 AI 기능이 탑재된 고성능 노트북, 가볍고 배터리가 오래 갑니다
			- 재고: 현재 구매 가능
			
			- 상품명: AI 스마트 워치
			- 가격: 300,000
			- 특징: 건강 모니터닐과 스마트폰 연동 기능 제공, 방수 기능 포함.
			- 재고: 품절(5일 후 재입고 예정)
			
			내부에 없는 정보 일 경우, 정중하게 안내하면서도 일반적인 정보나 유사한 내용을 최대한 제공해 주세요
			""";
	
	@Transactional
	public ChatResponseDto getChatResponse(String userId, String userMessage) {
		List<ChatMessage> previousMessages = 
				chatMessageRepository.findByUserIdOrderByCreatedAtAsc(userId);
		
		//이전 대화 이력을 스트림(Stream)으로 변환한다
		List<Message> history = previousMessages.stream()
				//사용자 보낸 메시지인지 확인한다 
				.map(cm -> cm.isUser() 
						//UserMessage는 Spring AI에서 사용자 입력을 표현하는 클래스
						? new UserMessage(cm.getContent())
						: new AssistantMessage(cm.getContent()))
				.collect(Collectors.toList());
		
		//시스템 메시지와 사용자 메시지를 포함 전체 대화 생성
		List<Message> conversion = new ArrayList<Message>();
		conversion.add(new SystemPromptTemplate(systemPrompt).createMessage());
		conversion.addAll(history);
		conversion.add(new UserMessage(userMessage));
		
		//AI모델에 요청
		Prompt prompt = new Prompt(conversion);
		ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
		String aiResponseMessage = response.getResult().getOutput().getText();
		
		//현재 질문과 답변을 DB 저장
		chatMessageRepository.save(new ChatMessage(userId, userMessage,true));
		chatMessageRepository.save(new ChatMessage(userId, aiResponseMessage,false));
		
		
		//JSON 구조로 변경하여 메시지 리스트로 생성
		List<ChatMessageDto> messages = new ArrayList<ChatMessageDto>();
		
		//이전 데이터 추가
		for(ChatMessage msg : previousMessages) {
			String sender = msg.isUser() ? "user":"ai";
			String content = msg.getContent();
			
			messages.add(new ChatMessageDto(sender, content));
		}
		
		// 현재 사용자 메시지 추가
		messages.add(new ChatMessageDto("user", userMessage));
		// AI 응답 메시지 추가
		messages.add(new ChatMessageDto("ai", aiResponseMessage));
		
		return new ChatResponseDto(messages);
		
	}
	
}










