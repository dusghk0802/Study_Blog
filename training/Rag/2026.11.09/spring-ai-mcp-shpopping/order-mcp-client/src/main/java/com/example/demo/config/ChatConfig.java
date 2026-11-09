package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {
	
	@Bean
	public ChatClient chatClient(ChatModel chatModel, SyncMcpToolCallbackProvider provider) {
		return ChatClient.builder(chatModel)
				.defaultToolCallbacks(provider.getToolCallbacks())
				.build();
	}

}
//.defaultToolCallbacks(provider.getToolCallbacks())
//spring.ai.mcp.client.streamable-http.connections.local-server.url=http://localhost:8090
//접근해서 ..@McpTool(name = "getOrder" 가지고 있는 )
