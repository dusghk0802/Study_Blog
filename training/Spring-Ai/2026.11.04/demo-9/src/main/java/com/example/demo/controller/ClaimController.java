package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ClaimDtos.ClaimResult;
import com.example.demo.service.ClaimService;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

	private final ClaimService claimService;

	public ClaimController(ClaimService claimService) {
		this.claimService = claimService;
	}

	@PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ClaimResult analyze(@RequestParam("receipt") MultipartFile receipt) {
		return claimService.analyze(receipt);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, String>> tooLarge() {
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
				.body(Map.of("message", "10MB 이하의 파일만 올릴 수 있습니다."));
	}
}
