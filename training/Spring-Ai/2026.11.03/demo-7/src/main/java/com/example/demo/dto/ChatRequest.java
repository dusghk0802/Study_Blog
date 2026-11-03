package com.example.demo.dto;

//화면에서 보내는 요청 (conversationId가 없으면 새 대화)
public record ChatRequest(String conversationId, String question, String model) {
}
