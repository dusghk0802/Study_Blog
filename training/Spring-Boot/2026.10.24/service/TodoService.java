package com.example.demo.service;

import com.example.demo.model.Todo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TodoService {
    // DB 대신 메모리에 저장 (서버 재시작하면 초기화됨)
    private final Map<Long, Todo> store = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public List<Todo> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Todo> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Todo save(Todo todo) {
        todo.setId(sequence.incrementAndGet());
        store.put(todo.getId(), todo);
        return todo;
    }

    public Optional<Todo> update(Long id, Todo request) {
        Todo todo = store.get(id);
        if (todo == null) return Optional.empty();
        todo.setTitle(request.getTitle());
        todo.setDone(request.isDone());
        return Optional.of(todo);
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
