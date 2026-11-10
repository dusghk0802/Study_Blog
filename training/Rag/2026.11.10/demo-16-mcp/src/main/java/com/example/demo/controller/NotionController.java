package com.example.demo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.NotionChatService;

@RestController
@RequestMapping(value = "/api", produces = MediaType.TEXT_PLAIN_VALUE)
public class NotionController {
    private final NotionChatService service;

    public NotionController(NotionChatService service) {
        this.service = service;
    }

    @GetMapping("/tools")
    public String tools() {
        return service.getTools();
    }

    @GetMapping("/ask")
    public String ask(@RequestParam("message") String message) {
        return service.ask(message);
    }
}
