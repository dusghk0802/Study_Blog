package com.example.demo.tool;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 카카오 로컬 API - 키워드로 장소 검색
 * GET https://dapi.kakao.com/v2/local/search/keyword.json
 * 헤더: Authorization: KakaoAK {REST API 키}
 */
@Component
public class KakaoLocalClient {

	private final RestClient restClient;
	private final String restApiKey;

	public KakaoLocalClient(@Value("${kakao.rest-api-key:}") String restApiKey) {
		this.restApiKey = restApiKey;
		this.restClient = RestClient.builder()
				.baseUrl("https://dapi.kakao.com")
				.build();
	}

	public boolean hasKey() {
		return restApiKey != null && !restApiKey.isBlank();
	}

	public List<Place> searchKeyword(String query, int size) {
		KeywordResponse response = restClient.get()
				.uri(uri -> uri.path("/v2/local/search/keyword.json")
						.queryParam("query", query)
						.queryParam("size", size)
						.build())
				.header("Authorization", "KakaoAK " + restApiKey)
				.retrieve()
				.body(KeywordResponse.class);
		return response == null || response.documents() == null ? List.of() : response.documents();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record KeywordResponse(List<Place> documents) {}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Place(
			@JsonProperty("place_name") String placeName,
			@JsonProperty("category_name") String categoryName,
			@JsonProperty("address_name") String addressName,
			@JsonProperty("road_address_name") String roadAddressName,
			@JsonProperty("phone") String phone,
			@JsonProperty("place_url") String placeUrl
	) {}
}
