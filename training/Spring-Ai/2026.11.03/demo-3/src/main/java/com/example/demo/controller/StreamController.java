package com.example.demo.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class StreamController {

    @GetMapping(
        value = "/stream",
        produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<String> stream() {

        return Flux.interval(Duration.ofSeconds(1))
                .map(i -> "서버 메시지 " + (i + 1) + " 도착")
                .take(5);
    }
}

/*
 * Flux.interval(Duration.ofSeconds(1))
 * 1초 간격으로 숫자 생성
 * 
 * .map(...)
 * 숫자를 메시지 문자열로 변환
 * 
 * .take(5)
 * 메시지 5개를 전송한 후 종료
 */