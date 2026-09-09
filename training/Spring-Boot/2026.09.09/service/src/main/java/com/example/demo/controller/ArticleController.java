package com.example.demo.controller;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<ArticleResponse> getAllArticles(){
        return articleService.findAll();
    }

    @GetMapping(params = "memberId")
    public List<ArticleResponse> getArticleByMemberId(
            @RequestParam("memberId") Long memberId){
        return articleService.findByMemberId(memberId);
    }
    // 게시글 한 건 조회
    @GetMapping("/{id}")
    public ArticleResponse getArticle(
            @PathVariable("id") Long id) {

        return articleService.findById(id);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ArticleResponse updateArticle(
            @PathVariable("id") Long id,
            @RequestBody ArticleRequest articleRequest) {

        return articleService.update(id, articleRequest);
    }
}
