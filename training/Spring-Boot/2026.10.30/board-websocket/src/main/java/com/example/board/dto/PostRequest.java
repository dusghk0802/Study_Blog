package com.example.board.dto;

/**
 * 글쓰기 요청 데이터 (브라우저 → 서버)
 */
public record PostRequest(
        String title,
        String content,
        String writer
) {
}
