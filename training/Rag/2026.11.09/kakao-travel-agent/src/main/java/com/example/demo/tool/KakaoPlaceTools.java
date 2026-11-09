package com.example.demo.tool;

import java.util.List;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 카카오 로컬 API 로 실제 장소를 검색하는 Tool.
 * 검색 결과의 장소 상세 URL 은 KakaoPlaceRegistry 에 기록되어, 일정 저장 시 검증에 쓰인다.
 */
@Slf4j
@Component
public class KakaoPlaceTools {

	private final KakaoLocalClient kakaoLocalClient;
	private final KakaoPlaceRegistry kakaoPlaceRegistry;

	public KakaoPlaceTools(KakaoLocalClient kakaoLocalClient, KakaoPlaceRegistry kakaoPlaceRegistry) {
		this.kakaoLocalClient = kakaoLocalClient;
		this.kakaoPlaceRegistry = kakaoPlaceRegistry;
	}

	public record PlaceResult(String placeName, String category, String address, String phone, String placeUrl) {}

	@Tool(description = """
			카카오맵에서 키워드로 실제 장소(맛집, 카페, 관광지, 숙소 등)를 검색한다.
			지역과 종류를 함께 넣어 검색하면 정확하다. 예: '강릉 안목해변 카페', '부산 해운대 돼지국밥'.
			결과의 placeUrl 이 장소 상세 페이지이며, 추천과 저장에는 이 결과만 사용한다.""")
	public List<PlaceResult> searchKakaoPlaces(
			@ToolParam(description = "검색어 (지역 + 장소 종류 또는 장소명)") String query,
			@ToolParam(description = "결과 개수 1~15, 기본 5", required = false) Integer size,
			ToolContext toolContext) {

		log.info("[Tool] searchKakaoPlaces {}", query);
		if (!kakaoLocalClient.hasKey()) {
			throw new IllegalStateException("카카오 REST API 키(KAKAO_REST_API_KEY)가 설정되지 않았습니다.");
		}
		int n = (size == null || size < 1) ? 5 : Math.min(size, 15);

		String conversationId = RecordingToolCallback.conversationId(toolContext);
		return kakaoLocalClient.searchKeyword(query, n).stream()
				.map(p -> {
					kakaoPlaceRegistry.record(conversationId, p.placeUrl());
					String address = (p.roadAddressName() != null && !p.roadAddressName().isBlank())
							? p.roadAddressName() : p.addressName();
					return new PlaceResult(p.placeName(), p.categoryName(), address, p.phone(),
							KakaoPlaceRegistry.toCanonical(p.placeUrl()));
				})
				.toList();
	}
}
