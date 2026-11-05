package com.example.demo.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	private String userId; //사용자 ID
	private String content; // 메시지
	private boolean isUser; // true 사용자 메시지, false 챗봇 메시지
	@Column(name = "created_at")
	private LocalDateTime createdAt; // 생성일
	
	public ChatMessage(String userId, String content, boolean isUser) {
		super();
		this.userId = userId;
		this.content = content;
		this.isUser = isUser;
		this.createdAt = LocalDateTime.now();
	}
	
	
}