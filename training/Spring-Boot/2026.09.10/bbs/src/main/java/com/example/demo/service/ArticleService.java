package com.example.demo.service;

import com.example.demo.dto.ArticleDto;
import com.example.demo.dto.ArticleForm;
import com.example.demo.model.Member;
import com.example.demo.model.Article;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;

    private ArticleDto mapToArticleDto(Article article){
        return ArticleDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .description(article.getDescription())
                .created(article.getCreated())
                .updated(article.getUpdated())
                .memberId(article.getMember().getId())
                .name(article.getMember().getName())
                .email(article.getMember().getEmail()).build();
    }

    public Page<ArticleDto> findAll(Pageable pageable) {

        return articleRepository.findAll(pageable)
                .map(this::mapToArticleDto);
    }

    public ArticleDto finById(Long id){
        return articleRepository.findById(id)
                .map(this::mapToArticleDto).orElseThrow();
    }

    public ArticleDto create(
            Long memberId,
            ArticleForm articleForm){

        Member member = memberRepository.findById(memberId).orElseThrow();

        Article article = Article.builder()
                .title(articleForm.getTitle())
                .description(articleForm.getDescription())
                .member(member)
                .build();

        articleRepository.save(article);
        return mapToArticleDto(article);
    }

    public ArticleDto update(ArticleForm articleForm){
        Article article = articleRepository
                .findById(articleForm.getId()).orElseThrow();

        article.setTitle(articleForm.getTitle());
        article.setDescription(articleForm.getDescription());

        articleRepository.save(article);
        return  mapToArticleDto(article);
    }

    public void delete(Long id){
        Article article = articleRepository
                .findById(id)
                .orElseThrow();
        articleRepository.delete(article);
    }
}
