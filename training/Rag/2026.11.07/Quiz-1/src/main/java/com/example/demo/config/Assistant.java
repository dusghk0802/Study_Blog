package com.example.demo.config;

/** 과제별 챗봇. 문서는 벡터 저장소의 domain 메타데이터로 분리한다. */
public enum Assistant {

	A("사내 규정 AI 챗봇", "회사의 취업규칙, 연차휴가 규정, 복리후생 문서를 안내하는 인사 담당자"),
	B("교육자료 AI 학습 도우미", "Java, Spring Boot, 데이터베이스 수업자료로 학습을 돕는 조교"),
	C("제품 매뉴얼 AI 상담사", "제품 사용설명서와 오류 해결 매뉴얼로 고객을 돕는 상담사");

	private final String title;
	private final String role;

	Assistant(String title, String role) {
		this.title = title;
		this.role = role;
	}

	public String title() {
		return title;
	}

	public String role() {
		return role;
	}
}
