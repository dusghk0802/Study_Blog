package com.example.board.repository;

import com.example.board.domain.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DB 대신 메모리(Map)에 게시글을 저장하는 저장소
 * 서버를 재시작하면 데이터는 사라진다.
 */
@Repository
public class PostRepository {

    // 여러 사용자가 동시에 글을 써도 안전하도록 ConcurrentHashMap, AtomicLong 사용
    private final Map<Long, Post> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Post save(String title, String content, String writer) {
        Long id = sequence.incrementAndGet();   // DB 시퀀스 역할
        Post post = new Post(id, title, content, writer, LocalDateTime.now());
        store.put(id, post);
        return post;
    }

    public List<Post> findAll() {
        return store.values().stream()
                .sorted(Comparator.comparing(Post::id).reversed())   // 최신 글이 위로
                .toList();
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
