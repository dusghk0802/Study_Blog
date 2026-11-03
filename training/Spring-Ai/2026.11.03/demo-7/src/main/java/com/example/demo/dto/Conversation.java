package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

//대화방 하나 (제목은 첫 질문으로 자동 생성)
public class Conversation {

	private String id;
	private String title;
	private String model;
	private String createdAt;
	private String updatedAt;
	private List<ChatMessage> messages = new ArrayList<>();

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getModel() { return model; }
	public void setModel(String model) { this.model = model; }
	public String getCreatedAt() { return createdAt; }
	public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
	public String getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
	public List<ChatMessage> getMessages() { return messages; }
	public void setMessages(List<ChatMessage> messages) { this.messages = messages; }
}
