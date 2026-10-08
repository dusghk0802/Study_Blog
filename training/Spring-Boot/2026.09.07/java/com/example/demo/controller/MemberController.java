package com.example.demo.controller;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @PostMapping
    public List<Member> post(@RequestBody List<Member> members){
        return memberRepository.saveAll(members);
    }

    @GetMapping
    public List<Member> getAll(){
        return memberRepository.findAll();
    }

    @GetMapping("/{id}")
    public Member get(@PathVariable("id")Long id){
        return memberRepository.findById(id).orElse(null);
    }

    //회원 수정하기
    @PutMapping("/{id}")
    public Member put(@PathVariable("id") Long id, @RequestBody Member member){
        member.setId(id);
        return memberRepository.save(member);
    }

    //회원 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
    memberRepository.deleteById(id);
    }
}
