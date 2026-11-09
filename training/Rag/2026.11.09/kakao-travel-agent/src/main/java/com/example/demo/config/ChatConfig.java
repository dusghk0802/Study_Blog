package com.example.demo.config;

import java.util.Arrays;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.tool.KakaoPlaceRegistry;
import com.example.demo.tool.KakaoPlaceTools;
import com.example.demo.tool.RecordingToolCallback;
import com.example.demo.tool.TravelPlanTools;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ChatConfig {

	private static final String SYSTEM_PROMPT = """
			당신은 한국 여행·맛집 추천 에이전트입니다.

			[도구 사용 규칙]
			1. 장소, 맛집, 카페, 관광지, 숙소는 반드시 searchKakaoPlaces 로 검색한 결과만 추천하세요.
			   검색 결과에 없는 장소, 주소, URL을 지어내지 마세요.
			   답변에는 장소 이름에 검색 결과의 placeUrl(장소 상세 페이지)을 링크로 걸어 보여주세요.
			   검색 결과가 마땅치 않으면 검색어를 바꿔 다시 검색하세요.
			2. 날짜가 나오면 항상 먼저 getToday 로 오늘 날짜를 확인하세요.
			   '내일', '이번 주말' 같은 상대 날짜와 '11월 12일'처럼 연도가 없는 날짜는
			   오늘 이후의 가장 가까운 날짜로 계산하세요. 연도를 추측하지 마세요.
			3. 사용자가 일정을 저장해 달라고 하면 saveTravelPlan 을 장소마다 한 번씩 호출하세요.
			   방문 순서(visitOrder)를 1부터 매기고, searchKakaoPlaces 결과의 주소와 placeUrl을 그대로 저장하세요.
			   장소 상세 URL이 없는 장소는 저장되지 않습니다. 저장이 거부되면 그 이유를 사용자에게 알려주세요.
			   저장 요청이 아니면 저장 관련 도구를 호출하지 마세요.
			4. 저장된 일정 조회는 findTravelPlans, 삭제는 deleteTravelPlan 을 사용하세요.

			[답변 형식]
			- 추천은 3~5곳, 각 장소마다 이름 / 분류 / 주소 / 추천 이유를 적으세요.
			- 동선이 자연스럽도록 가까운 곳끼리 묶어 순서를 제안하세요.
			- 마지막에 "이 일정을 저장할까요?" 처럼 다음 행동을 제안하세요.
			- 한국어 마크다운으로 간결하게 답변하세요.
			""";

	@Bean
	public ChatMemory chatMemory() {
		return MessageWindowChatMemory.builder().maxMessages(20).build();
	}

	@Bean
	public ChatClient chatClient(ChatModel chatModel,
			ObjectProvider<SyncMcpToolCallbackProvider> mcpToolProvider, // 카카오맵 MCP Tool (원격)
			KakaoPlaceTools kakaoPlaceTools,                             // 카카오 로컬 API 장소 검색 Tool
			TravelPlanTools travelPlanTools,                             // Oracle 일정 저장 Tool (로컬)
			KakaoPlaceRegistry kakaoPlaceRegistry,                       // 카카오맵이 돌려준 장소 URL 기록
			ChatMemory chatMemory) {

		ChatClient.Builder builder = ChatClient.builder(chatModel)
				.defaultSystem(SYSTEM_PROMPT)
				.defaultTools(kakaoPlaceTools, travelPlanTools)
				.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build());

		// MCP 연결이 켜져 있으면 카카오맵 Tool 들을 함께 등록
		// (결과에 들어 있는 장소 URL 을 기록하도록 감싸서 등록)
		mcpToolProvider.ifAvailable(provider -> {
			ToolCallback[] callbacks = Arrays.stream(provider.getToolCallbacks())
					.map(c -> (ToolCallback) new RecordingToolCallback(c, kakaoPlaceRegistry))
					.toArray(ToolCallback[]::new);
			log.info("카카오 PlayMCP Tool {}개 등록: {}", callbacks.length,
					Arrays.stream(callbacks).map(c -> c.getToolDefinition().name()).toList());
			builder.defaultToolCallbacks(callbacks);
		});

		return builder.build();
	}
}
