package com.example.demo.controller;

import com.example.demo.model.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {
    private final List<Member> members = List.of(
            Member.builder().id(1L).name("윤서준").email("SeojunYoon@hanbit.co.kr").build(),
            Member.builder().id(2L).name("윤광철").email("KwangcheilYoon@hanbit.co.kr").build(),
            Member.builder().id(3L).name("공미영").email("MiyoungKong@hanbit.co.kr").build(),
            Member.builder().id(4L).name("김도윤").email("Doyoonkim@hanbit.co.kr").build());

    @GetMapping("/api/members")
    public List<Member> getMembers(){
        return members;
    }
}
