package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dto.ChatRequestDto;
import com.example.demo.dto.ChatResponseDto;
import com.example.demo.service.CustomerSupportService;

@RestController
public class ChatController {

	private final CustomerSupportService service;
	
	public ChatController(CustomerSupportService service) {
		this.service = service;
	}
	
	@PostMapping("/chat")
	public ChatResponseDto chat(@RequestBody ChatRequestDto request) {
		System.out.println("요청된 request 값:" + request.getUserId());
		ChatResponseDto responseMessage = 
				service.getChatResponse(request.getUserId(), request.getMessage());
		return responseMessage;
	}
	
}
