package com.example.demo.dto;

//선택 가능한 Ollama 모델 (parameterSize : 3.9B 같은 모델 크기)
public record ModelInfo(String name, String parameterSize, Long size) {
}
