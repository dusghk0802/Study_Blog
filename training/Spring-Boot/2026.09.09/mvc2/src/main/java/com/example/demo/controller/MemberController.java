package com.example.demo.controller;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/member/add") //회원 추가를 위한 from 화면
    public String getMemberAdd(){
        return "member-form";
    }

    @PostMapping("/member/add")
    public String postMemberAdd(Member member){ //회원 푸가를 위한 post 방식
        memberRepository.save(member);
        return "redirect:/home";
    }

    @GetMapping("/member/edit")
    public String getMemberEdit(@RequestParam("id") Long id, Model model){
        Member member = memberRepository.findById(id).orElseThrow();
        model.addAttribute("member", member);
        return "member-edit-form";
    }

    @PostMapping("/member/edit")
    public String postMemberEdit(Member member){
        memberRepository.save(member);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String getHome(){
        return "home";
    }
}
