package com.example.demo.service;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

@Service
public class NotionChatService {
    private final ChatClient chatClient;
    private final SyncMcpToolCallbackProvider provider;

    public NotionChatService(ChatClient chatClient, SyncMcpToolCallbackProvider provider) {
        this.chatClient = chatClient;
        this.provider = provider;
    }

    public String getTools() {
        return Arrays.stream(provider.getToolCallbacks())
                .map(t -> t.getToolDefinition().name() + " : " + t.getToolDefinition().description())
                .collect(Collectors.joining("\n\n"));
    }

    public String ask(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("질문을 입력하세요.");
        }
        ToolCallback[] readTools = Arrays.stream(provider.getToolCallbacks())
                .filter(t -> isReadTool(t.getToolDefinition().name()))
                .toArray(ToolCallback[]::new);
        if (readTools.length == 0) {
            return "조회용 Notion MCP Tool을 찾지 못했습니다. /api/tools에서 실제 이름을 확인하세요.";
        }
        return chatClient.prompt()
                .system("""
                    당신은 Notion 문서 검색 및 요약 도우미입니다.
                    제공된 조회 도구로 실제 문서를 검색한 뒤 답변하세요.
                    문서 생성, 수정, 삭제를 수행하지 마세요.
                    확인되지 않은 내용은 만들어내지 마세요.
                    한국어로 답변하세요.
                    """)
                .user(question)
                .toolCallbacks(readTools)
                .call()
                .content();
    }

    // Classroom heuristic only. Inspect /api/tools and replace with an explicit allowlist for production.
    private boolean isReadTool(String name) {
        String n = name.toLowerCase(Locale.ROOT);
        return n.contains("search") || n.contains("fetch") ||
               n.contains("retrieve") || n.contains("list");
    }
}