package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ChatService;

@RestController
public class ChatController {
	
	private final ChatService chatService;
	
	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}
	
	@GetMapping("/ask")
	public String ask(@RequestParam("question") String question) {
		return chatService.chat(question);
	}
}
