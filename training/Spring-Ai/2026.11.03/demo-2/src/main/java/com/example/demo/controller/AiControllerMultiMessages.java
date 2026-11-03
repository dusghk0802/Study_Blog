package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.example.demo.service.AiServiceMultiMessages;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ai")
@Slf4j
public class AiControllerMultiMessages {

	//서비스에 의존합니다
	@Autowired
	private AiServiceMultiMessages aiService;
	
	//요청 처리
	@PostMapping(
		value="/multi-messages",
		consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
		produces = MediaType.TEXT_PLAIN_VALUE
	)
	public String multiMessages(@RequestParam("question") String question , HttpSession session) {
		
		List<Message>  chatMemory = (List<Message>)session.getAttribute("chatMemory");
		if(chatMemory == null) {
			chatMemory = new ArrayList<>();
			session.setAttribute("chatMemory", chatMemory);
		}
		
		//서비스 호출
		String answer = aiService.multiMessages(question, chatMemory);
		
		return answer;
	}
}

/*
	consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 요청 데이터 형식 : 클라이언트가 서버에 보내는 형식 정의
	클라이언트가 데이터를 HTML form 형식으로 전송'
	'application/x-www-form-urlencoded',  >> .APPLICATION_FORM_URLENCODED_VALUE
	
	
	produces = MediaType.TEXT_PLAIN_VALUE , 응답 데이터 형식 : 서버가 클라이언트에게 반환하는 데이터 형식

*/



