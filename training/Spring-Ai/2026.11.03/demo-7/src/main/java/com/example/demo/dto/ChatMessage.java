package com.example.demo.dto;

//대화 한 건 (role : user / assistant, model : 답변한 모델)
public record ChatMessage(String role, String text, String model, String time) {
}
