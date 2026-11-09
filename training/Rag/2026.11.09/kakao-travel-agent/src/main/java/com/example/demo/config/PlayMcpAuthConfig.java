package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;

/**
 * 카카오 PlayMCP 게이트웨이는 Bearer 액세스 토큰이 있어야 접속할 수 있다.
 * MCP Client가 보내는 모든 HTTP 요청에 Authorization 헤더를 붙인다.
 */
@Configuration
@ConditionalOnProperty(name = "spring.ai.mcp.client.enabled", havingValue = "true", matchIfMissing = true)
public class PlayMcpAuthConfig {

	@Bean
	public McpSyncHttpClientRequestCustomizer playMcpAuthCustomizer(
			@Value("${kakao.playmcp.token}") String token) {

		if (token == null || token.isBlank()) {
			throw new IllegalStateException(
					"환경변수 KAKAO_PLAYMCP_TOKEN 이 없습니다. PlayMCP 액세스 토큰을 먼저 설정하세요.");
		}

		return (builder, method, endpoint, body, context) ->
				builder.header("Authorization", "Bearer " + token);
	}
}
