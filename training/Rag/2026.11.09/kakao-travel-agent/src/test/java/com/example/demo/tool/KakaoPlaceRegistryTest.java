package com.example.demo.tool;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.DefaultToolDefinition;
import org.springframework.ai.tool.definition.ToolDefinition;

class KakaoPlaceRegistryTest {

	// 카카오맵 MCP 도구 대신 쓰는 가짜 도구 (장소 검색 결과 JSON 을 돌려줌)
	static class FakeKakaoSearchTool implements ToolCallback {
		@Override
		public ToolDefinition getToolDefinition() {
			return DefaultToolDefinition.builder().name("searchPlace").description("test").inputSchema("{}").build();
		}

		@Override
		public String call(String toolInput) {
			return """
					[{"place_name":"감천문화마을","road_address_name":"부산 사하구 감내2로 203",
					  "place_url":"http://place.map.kakao.com/8216870"}]
					""";
		}
	}

	@Test
	void 카카오맵이_돌려준_URL만_저장_허용() {
		KakaoPlaceRegistry registry = new KakaoPlaceRegistry();
		ToolCallback tool = new RecordingToolCallback(new FakeKakaoSearchTool(), registry);

		tool.call("{}", new ToolContext(Map.of(RecordingToolCallback.CONVERSATION_ID, "c1")));

		// 검색 결과에 있던 URL (http/https 모두 허용)
		assertThat(registry.isVerified("c1", "https://place.map.kakao.com/8216870")).isTrue();
		assertThat(registry.isVerified("c1", "http://place.map.kakao.com/8216870")).isTrue();
		// AI 가 지어낸 URL
		assertThat(registry.isVerified("c1", "https://place.map.kakao.com/11505559")).isFalse();
		// 다른 대화에서는 인정 안 함
		assertThat(registry.isVerified("c2", "https://place.map.kakao.com/8216870")).isFalse();
		// URL 없음
		assertThat(registry.isVerified("c1", null)).isFalse();
	}
}
