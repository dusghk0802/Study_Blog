package com.example.demo;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JpaApplication implements ApplicationRunner {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var member = Member.builder()
                .name("윤서준")
                .email("SeoJunYoon@hanbit.co.kr")
                .age(10).build();

        memberRepository.save(member);
        log.info("회원 = {}", member);

        member = Member.builder()
                .name("윤광철")
                .email("KwangcheolYoon@hanbit.co.kr")
                .age(43).build();
        memberRepository.save(member);
/*        var members = memberRepository.findAll();
        log.info("회원목록={}", members);*/

        var members = memberRepository.findByEmail("KwangcheolYoon@hanbit.co.kr");
        log.info("회원목록 = {}", members);

        //나이가 30보다 큰 회원 조회
        var member1 = memberRepository.findByAgeGreaterThan(30);
        log.info("30세 초과 회원={}", member1);

        /*//이메일로 회원 삭제
        int deleteCount1 = memberRepository.deleteByEmail("KwangcheolYoon@hanbit.co.kr");
        log.info("이메일로 삭제한 회원 {}", deleteCount1);

        //이름으로 회원 삭제
        int deleteCount2 = memberRepository.deleteByName("윤서준");
        log.info("이름으로 삭제한 회원 {}", deleteCount2);

        //삭제 후 전체 회원 조회
        var afterDeletmembers = memberRepository.findAll();
        log.info("삭제 후 회원 목록 {}", afterDeletmembers);*/

        member = Member.builder()
                .name("이코사")
                .age(10).build();

        memberRepository.save(member);
        Example<Member> example = Example.of(member);

        var members1 = memberRepository.findAll(example);
        log.info("회원 목록={}", members1);
    }
}
