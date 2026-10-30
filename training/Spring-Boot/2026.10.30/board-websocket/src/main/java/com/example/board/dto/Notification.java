package com.example.board.dto;

import java.time.LocalDateTime;

/**
 * 실시간 알림 메시지 (서버 → 구독 중인 모든 브라우저)
 */
public record Notification(
        Long postId,
        String title,
        String writer,
        String message,
        LocalDateTime sentAt
) {
}
