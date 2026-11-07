package com.example.demo.service;

/** 사용자가 고칠 수 있는 업로드 오류. 메시지를 그대로 화면에 보여 준다. */
public class InvalidPdfException extends RuntimeException {

	public InvalidPdfException(String message) {
		super(message);
	}
}
