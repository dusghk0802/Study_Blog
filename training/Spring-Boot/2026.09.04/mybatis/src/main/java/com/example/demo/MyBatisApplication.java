package com.example.demo;

import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.MemberMapper;
import com.example.demo.model.Article;
import com.example.demo.model.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyBatisApplication implements ApplicationRunner {
    @Autowired
    private MemberMapper memberMapper; //의존성 주입

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception{
/*        var members = memberMapper.selectAll();
        log.info("회원 목록 ={}", members);
        log.info("-----------------------");
        var member = memberMapper.selectById(3L);
        log.info("3번 회원 = {}", member);*/

        // 1. 이름에 '서'를 포함한 사람 조회
/*        var members = memberMapper.selectByName("서");
        log.info("이름에 '서'를 포함한 회원 = {}", members);*/

        // 2. 새로운 회원 생성
/*        Member member = new Member();
        member.setName("김민준");
        member.setEmail("minjun@test.com");
        member.setAge(25);*/

/*        memberMapper.insertMember(member);
        log.info("회원 추가 완료 = {}", member);*/

        // 3. 회원 추가 후 전체 회원 다시 조회
        var allMembers = memberMapper.selectAll();
        log.info("전체 회원 = {}", allMembers);

        var articles = articleMapper.selectAll();
        log.info("게시글 목록={}", articles);
        log.info("----------------");
        var article = articleMapper.selectById(1L);
        log.info("1번 게시글={}", article);
    }

}
