package com.example.demo.repository;

import com.example.demo.entity.Member;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp(){
        memberRepository.save(Member.builder()
                .name("윤서준")
                .email("Seojun@kosa.or.kr")
                .age(10)
                .build());

        memberRepository.save(Member.builder()
                .name("윤광철")
                .email("kwangcheol@kosa.or.kr")
                .age(10)
                .build());
    }
    @AfterEach
    void tearDown(){
        memberRepository.deleteAll();
    }
    /*@RepeatedTest(3)
    @Disabled("잠시 중단")*/
    @Test
    void 회원_전체조회_테스트(){
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);
    }
}
