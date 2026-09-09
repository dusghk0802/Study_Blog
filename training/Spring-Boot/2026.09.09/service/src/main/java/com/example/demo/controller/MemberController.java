package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.entity.Member;
import com.example.demo.service.ArticleService;
import com.example.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private ArticleService articleService;
    @PostMapping("/{id}/articles")
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse postArticle(
            @PathVariable(value = "id") Long id,
            @RequestBody ArticleRequest req){
        return articleService.create(id, req);
    }

    @PostMapping
    public List<MemberResponse> post(@RequestBody List<MemberRequest> memberRequest){
        return memberService.createBatch(memberRequest);
    }
    @GetMapping()
    public List<MemberResponse> get(){
        return memberService.findAll();
    }

    //id 검색, 수정, 삭제 추가
    //postman으로 확인
    @GetMapping("{id}")
//    public Member get(@PathVariable("id") Long id){
    public MemberResponse get(@PathVariable("id") Long id){
        return memberService.findById(id);
    }

    @PutMapping("/{id}")
    public Member update(@PathVariable("id") Long id,
                         @RequestBody Member member){
        member.setId(id);
        return memberService.save(member);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        memberService.deleteById(id);
    }
}
