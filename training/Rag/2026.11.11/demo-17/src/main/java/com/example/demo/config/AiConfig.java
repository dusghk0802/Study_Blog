package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration  //자바용 설정 파일 입니다(xml 설정)
public class AiConfig {

	@Bean
    public ChatClient chatClient(
            ChatClient.Builder builder,
            ToolCallbackProvider mcpTools) {

        return builder
                .defaultSystem("""
                    당신은 Spring AI 기술 문서 분석 Agent입니다.

                    사용자가 GitHub 프로젝트에 대해 질문하면
                    가능한 경우 DeepWiki MCP Tool을 사용하세요.

                    기본 분석 대상 저장소는
                    spring-projects/spring-ai 입니다.

                    MCP Tool로 확인한 내용과
                    일반적인 모델 지식을 구분하세요.

                    답변은 한국어로 작성하세요.
                    """)
                .defaultToolCallbacks(mcpTools)
                .build();
    }
}


