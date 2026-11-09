package com.example.demo.tool;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * 카카오맵 MCP 도구가 실제로 돌려준 장소 상세 URL 을 대화별로 기억한다.
 * 일정 저장 시 이 목록에 있는 URL 만 허용해서, AI 가 지어낸 장소/링크가 저장되지 않게 한다.
 */
@Component
public class KakaoPlaceRegistry {

	private static final Pattern PLACE_URL = Pattern.compile("https?://place\\.map\\.kakao\\.com/(\\d+)");

	private final Map<String, Set<String>> placeUrlsByConversation = new ConcurrentHashMap<>();

	/** MCP 도구 결과 문자열에서 장소 URL 을 찾아 기록 */
	public void record(String conversationId, String toolResult) {
		if (toolResult == null) return;
		Matcher m = PLACE_URL.matcher(toolResult);
		while (m.find()) {
			placeUrlsByConversation
					.computeIfAbsent(conversationId, k -> ConcurrentHashMap.newKeySet())
					.add(normalize(m.group(1)));
		}
	}

	/** 이 대화에서 카카오맵이 실제로 돌려준 URL 인지 확인 */
	public boolean isVerified(String conversationId, String placeUrl) {
		if (placeUrl == null) return false;
		Matcher m = PLACE_URL.matcher(placeUrl.trim());
		if (!m.matches()) return false;
		return placeUrlsByConversation.getOrDefault(conversationId, Set.of()).contains(normalize(m.group(1)));
	}

	public static String normalize(String placeId) {
		return "https://place.map.kakao.com/" + placeId;
	}

	public static String toCanonical(String placeUrl) {
		Matcher m = PLACE_URL.matcher(placeUrl.trim());
		return m.matches() ? normalize(m.group(1)) : placeUrl.trim();
	}
}
