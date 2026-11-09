package com.example.demo.tool;

import java.time.LocalDate;
import java.util.List;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.example.demo.dto.TravelPlan;
import com.example.demo.repository.TravelPlanRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 자체 Oracle DB 에 여행 일정을 저장/조회/삭제하는 로컬 Tool.
 * 카카오맵 MCP Tool(장소 검색)과 함께 ChatClient 에 등록된다.
 */
@Slf4j
@Component
public class TravelPlanTools {

	private final TravelPlanRepository travelPlanRepository;
	private final KakaoPlaceRegistry kakaoPlaceRegistry;

	public TravelPlanTools(TravelPlanRepository travelPlanRepository, KakaoPlaceRegistry kakaoPlaceRegistry) {
		this.travelPlanRepository = travelPlanRepository;
		this.kakaoPlaceRegistry = kakaoPlaceRegistry;
	}

	@Tool(description = "오늘 날짜를 yyyy-MM-dd 형식으로 반환. '내일', '이번 주말' 같은 상대 날짜를 계산할 때 먼저 호출")
	public String getToday() {
		log.info("[Tool] getToday");
		return LocalDate.now().toString();
	}

	@Tool(description = "추천한 장소 하나를 여행 일정으로 DB에 저장. 장소가 여러 개면 장소마다 한 번씩 호출")
	public String saveTravelPlan(
			@ToolParam(description = "여행 이름 (예: 부산 1박2일)") String tripTitle,
			@ToolParam(description = "방문 날짜 yyyy-MM-dd. 연도는 getToday 결과를 기준으로 계산") String tripDate,
			@ToolParam(description = "그날의 방문 순서 (1부터)", required = false) Integer visitOrder,
			@ToolParam(description = "장소명") String placeName,
			@ToolParam(description = "분류. 반드시 맛집, 카페, 관광지, 숙소 중 한 단어 (주소를 넣지 말 것)", required = false) String category,
			@ToolParam(description = "장소 주소 (예: 부산 해운대구 해운대해변로 264)", required = false) String address,
			@ToolParam(description = "카카오맵 검색 결과의 장소 상세 URL (https://place.map.kakao.com/숫자). 필수") String placeUrl,
			@ToolParam(description = "추천 이유나 메모", required = false) String memo,
			ToolContext toolContext) {

		log.info("[Tool] saveTravelPlan {} {} {} {}", tripTitle, tripDate, placeName, placeUrl);

		// 카카오맵 도구가 이 대화에서 실제로 돌려준 장소 URL 인 경우에만 저장 (지어낸 링크 차단)
		String conversationId = RecordingToolCallback.conversationId(toolContext);
		if (!kakaoPlaceRegistry.isVerified(conversationId, placeUrl)) {
			log.warn("[Tool] saveTravelPlan 거부 - 카카오맵 검색 결과에 없는 URL: {}", placeUrl);
			return ("저장 거부: '%s' 의 URL은 카카오맵 검색 결과에서 확인되지 않았습니다. "
					+ "카카오맵에서 검색된 장소만, 검색 결과의 장소 URL 그대로 저장할 수 있습니다. URL을 지어내지 마세요.")
					.formatted(placeName);
		}
		placeUrl = KakaoPlaceRegistry.toCanonical(placeUrl);

		LocalDate date;
		try {
			date = LocalDate.parse(tripDate);
		} catch (Exception e) {
			return "날짜 형식 오류: tripDate 는 yyyy-MM-dd 형식이어야 합니다.";
		}
		try {
			travelPlanRepository.save(tripTitle, date, visitOrder, placeName, category, address, placeUrl, memo);
		} catch (Exception e) {
			log.error("[Tool] saveTravelPlan 실패", e);
			return "저장 실패: " + e.getMessage();
		}
		return "저장 완료: [%s] %s %s".formatted(tripTitle, tripDate, placeName);
	}

	@Tool(description = "DB에 저장된 여행 일정 조회. 여행 이름 키워드가 없으면 전체 조회")
	public List<TravelPlan> findTravelPlans(
			@ToolParam(description = "여행 이름 검색 키워드", required = false) String keyword) {

		log.info("[Tool] findTravelPlans {}", keyword);
		if (keyword == null || keyword.isBlank()) {
			return travelPlanRepository.findAll();
		}
		return travelPlanRepository.findByTitle(keyword);
	}

	@Tool(description = "여행 일정 하나를 planId 로 삭제")
	public String deleteTravelPlan(@ToolParam(description = "삭제할 일정의 planId") Long planId) {
		log.info("[Tool] deleteTravelPlan {}", planId);
		int count = travelPlanRepository.deleteById(planId);
		return count > 0 ? "삭제 완료: planId=" + planId : "해당 planId 일정이 없습니다: " + planId;
	}
}
