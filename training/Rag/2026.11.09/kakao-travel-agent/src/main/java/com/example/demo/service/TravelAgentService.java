package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TravelPlan;
import com.example.demo.repository.TravelPlanRepository;
import com.example.demo.tool.RecordingToolCallback;

@Service
public class TravelAgentService {

	private final ChatClient chatClient;
	private final TravelPlanRepository travelPlanRepository;

	public TravelAgentService(ChatClient chatClient, TravelPlanRepository travelPlanRepository) {
		this.chatClient = chatClient;
		this.travelPlanRepository = travelPlanRepository;
	}

	public String chat(String conversationId, String message) {
		return chatClient.prompt()
				.user(message)
				.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
				// 도구들이 어느 대화의 요청인지 알 수 있게 전달
				.toolContext(Map.of(RecordingToolCallback.CONVERSATION_ID, conversationId))
				.call()
				.content();
	}

	public List<TravelPlan> getPlans() {
		return travelPlanRepository.findAll();
	}

	public void deletePlan(Long planId) {
		travelPlanRepository.deleteById(planId);
	}
}
