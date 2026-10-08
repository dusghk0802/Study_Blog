package com.example.demo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogApplication {
    public static void main(String[] args) {
        log.info("프로그램이 시작되었습니다");
        log.warn("경고 메시지입니다");
        log.error("오류 메시지 입니다.");
    }

}
