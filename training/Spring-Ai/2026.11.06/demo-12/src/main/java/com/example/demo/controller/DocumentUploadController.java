package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.HotelEmbeddingService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotel")
public class DocumentUploadController {

	private final HotelEmbeddingService embeddingService;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadText(@RequestParam("file") MultipartFile file){
		try {
			embeddingService.processUploadText(file); //임베딩 hotel_vector
			return ResponseEntity.ok("Text 업로드 정상...임베딩 완료...");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								.body("임베딩 처리중 오류 발생 :" + e.getMessage());
		}
	}
}
