package com.example.board.controller;

import com.example.board.domain.Post;
import com.example.board.dto.Notification;
import com.example.board.dto.PostRequest;
import com.example.board.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final SimpMessagingTemplate messagingTemplate;   // 서버에서 WebSocket 메시지를 보내는 도구

    public PostController(PostRepository postRepository, SimpMessagingTemplate messagingTemplate) {
        this.postRepository = postRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // 글 목록
    @GetMapping
    public List<Post> list() {
        return postRepository.findAll();
    }

    // 글 상세
    @GetMapping("/{id}")
    public Post detail(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다."));
    }

    // 글 작성 → 저장 후 실시간 알림 발송
    @PostMapping
    public ResponseEntity<Post> write(@RequestBody PostRequest request) {
        if (isBlank(request.title()) || isBlank(request.content())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "제목과 내용은 필수입니다.");
        }
        String writer = isBlank(request.writer()) ? "익명" : request.writer().trim();

        Post post = postRepository.save(request.title().trim(), request.content().trim(), writer);

        // /topic/notifications 를 구독 중인 모든 브라우저에게 알림 전송
        Notification notification = new Notification(
                post.id(),
                post.title(),
                post.writer(),
                post.writer() + "님이 새 글을 작성했습니다.",
                LocalDateTime.now()
        );
        messagingTemplate.convertAndSend("/topic/notifications", notification);

        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
