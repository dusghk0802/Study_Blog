package com.example.demo.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class AiServiceMultiMessages {
	
	private ChatClient chatClient;
	
	public AiServiceMultiMessages(ChatClient.Builder chatClientBuilder) {
		this.chatClient = chatClientBuilder.build();
	}
	
	//원하는 기능 LLM 연동
	public String multiMessages(String question , List<Message> chatMemory) {
		//LLM 연동 (Message 보내고 받기)
		
		SystemMessage systemMessage = SystemMessage
				                     .builder()
				                     .text("""
				                     		당신은 AI 비서입니다.
				                     		제공되는 지난 대화 내용을 보고 우선적으로 답변해주세요.
				                     		""")
				                     .build();
		//대화 처음 시작시 , 시스템 메시지 저장
		if(chatMemory.size() == 0) {
			chatMemory.add(systemMessage);
		}
		//console에 값을 출력
		log.info(chatMemory.toString());
		
		//LLM 이야기 하기
		ChatResponse chatResponse = chatClient.prompt()
									          .messages(chatMemory) //이전대화내용 추가
									          .user(question) //사용자 메시지
									          .call() //동기식
									          .chatResponse();
		
		UserMessage userMessage = UserMessage.builder().text(question).build();
		chatMemory.add(userMessage);
		
		//AI 답한 것
		AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
		chatMemory.add(assistantMessage);
		
		
		//LLM 텍스트 변환해서 전달
		String text = assistantMessage.getText();
		
		return text;
	}
	
}





