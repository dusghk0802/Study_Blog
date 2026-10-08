package com.example.demo.service;

import com.example.demo.dto.ArticleRequest;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.entity.Article;
import com.example.demo.entity.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    private ArticleResponse mapToArticleResponse(Article article){
        return ArticleResponse.builder()
                .id(article.getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .build();
    }
    public ArticleResponse create(Long memberId, ArticleRequest req){
        Member member = memberRepository.findById(memberId).orElseThrow();
        Article article = Article.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .member(member)
                .build();

        articleRepository.save(article);
        return mapToArticleResponse(article);
    }
    public List<ArticleResponse> findAll(){
            return articleRepository.findAll()
                    .stream()
                    .map(this::mapToArticleResponse)
                    .toList();
    }

    public List<ArticleResponse> findByMemberId(Long memberId) {
        return articleRepository.findAll()
                .stream()
                .filter(article -> article.getMember().getId().equals(memberId))
                .map(this::mapToArticleResponse)
                .toList();
    }

    public ArticleResponse findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow();

        return mapToArticleResponse(article);
    }

    public ArticleResponse update(Long id, ArticleRequest req){
        Article article = articleRepository.findById(id)
                .orElseThrow();
        article.setTitle(req.getTitle());
        article.setDescription(req.getDescription());

        articleRepository.save(article);

        return mapToArticleResponse(article);
    }
}
