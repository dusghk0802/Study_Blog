package com.example.demo.controller;

import com.example.demo.model.Todo;
import com.example.demo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor // final 필드 생성자 주입
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public List<Todo> list() {
        return todoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> get(@PathVariable Long id) {
        return todoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.save(todo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo todo) {
        return todoService.update(id, todo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return todoService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
