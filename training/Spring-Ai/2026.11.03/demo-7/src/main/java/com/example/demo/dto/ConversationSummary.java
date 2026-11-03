package com.example.demo.dto;

//왼쪽 대화 목록에 보여줄 요약
public record ConversationSummary(String id, String title, String model, String updatedAt, int messageCount) {

	public static ConversationSummary of(Conversation c) {
		return new ConversationSummary(c.getId(), c.getTitle(), c.getModel(), c.getUpdatedAt(), c.getMessages().size());
	}
}
