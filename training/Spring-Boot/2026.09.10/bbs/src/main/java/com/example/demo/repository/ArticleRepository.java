package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.Article;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Transactional
    void deleteAllByMember(Member member);
}
