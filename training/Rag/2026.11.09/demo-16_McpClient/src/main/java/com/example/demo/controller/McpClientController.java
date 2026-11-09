package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mcp")
public class McpClientController {

	private final ChatClient chatClient;

    @GetMapping("/ask")
    public String ask(@RequestParam("message") String message) {
    	System.out.println("ask Call...");
    	String systemPrompt = """
    			You are allowed to use the MCP tool only when the user asks for a greeting
    			such as 'hello', 'hi', '안녕', or '인사해'.
    			Do NOT call the tool for any other request.
    			""";

        return chatClient
                .prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();
    }
}