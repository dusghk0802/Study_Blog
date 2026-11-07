package com.example.demo.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.example.demo.service.InvalidPdfException;

@RestControllerAdvice
public class ApiExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler(InvalidPdfException.class)
	public ResponseEntity<Map<String, String>> invalidPdf(InvalidPdfException e) {
		return message(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<Map<String, String>> missingFile() {
		return message(HttpStatus.BAD_REQUEST, "PDF 파일을 선택해 주세요.");
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Map<String, String>> tooLarge() {
		return message(HttpStatus.PAYLOAD_TOO_LARGE, "20MB 이하의 PDF만 올릴 수 있습니다.");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Map<String, String>> unknownAssistant() {
		return message(HttpStatus.NOT_FOUND, "없는 과제입니다.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> unexpected(Exception e) {
		log.error("Request failed", e);
		return message(HttpStatus.INTERNAL_SERVER_ERROR, "요청을 처리하지 못했습니다. 잠시 후 다시 시도해 주세요.");
	}

	private ResponseEntity<Map<String, String>> message(HttpStatus status, String text) {
		return ResponseEntity.status(status).body(Map.of("message", text));
	}
}
