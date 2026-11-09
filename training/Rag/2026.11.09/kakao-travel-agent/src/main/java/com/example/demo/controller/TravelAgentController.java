package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.TravelPlan;
import com.example.demo.service.TravelAgentService;

@RestController
@RequestMapping("/api/travel")
public class TravelAgentController {

	private final TravelAgentService travelAgentService;

	public TravelAgentController(TravelAgentService travelAgentService) {
		this.travelAgentService = travelAgentService;
	}

	public record ChatRequest(String conversationId, String message) {}

	// 에이전트와 대화 (카카오맵 검색 + 일정 저장 Tool 호출)
	@PostMapping("/chat")
	public Map<String, String> chat(@RequestBody ChatRequest request) {
		String conversationId = (request.conversationId() == null || request.conversationId().isBlank())
				? "default" : request.conversationId();
		String answer = travelAgentService.chat(conversationId, request.message());
		return Map.of("answer", answer);
	}

	// 저장된 여행 일정 목록 (화면 오른쪽 패널)
	@GetMapping("/plans")
	public List<TravelPlan> plans() {
		return travelAgentService.getPlans();
	}

	@DeleteMapping("/plans/{planId}")
	public void deletePlan(@PathVariable("planId") Long planId) {
		travelAgentService.deletePlan(planId);
	}
}
