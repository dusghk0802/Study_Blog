package com.example.demo.controller;

import com.example.demo.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MemberController {
    @GetMapping("/member")
    public String getMember(Model model){
        var member = Member.builder()
                .id(1L)
                .name("윤서준")
                .email("Yoon@kosa.or.kr")
                .age(10).build();
        model.addAttribute("member", member);
        return "member-info";
    }
    @GetMapping("/member/list")
    public String getMemberList(Model model){
        var memberlist = List.of(
                Member.builder().id(1L).name("윤서준").email("Yoon@kosa.or.kr").age(10).build(),
                Member.builder().id(1L).name("윤광철").email("Yoon@kosa.or.kr").age(10).build(),
                Member.builder().id(1L).name("공미영").email("Yoon@kosa.or.kr").age(10).build(),
                Member.builder().id(1L).name("윤서").email("Yoon@kosa.or.kr").age(10).build()
        );
        model.addAttribute("memberList",memberlist);
        return "member-list";
    }
}
