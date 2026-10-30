package com.example.board.domain;

import java.time.LocalDateTime;

/**
 * 게시글
 */
public record Post(
        Long id,
        String title,
        String content,
        String writer,
        LocalDateTime createdAt
) {
}
