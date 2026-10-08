package com.example.demo.serivce;

import com.example.demo.dto.MemberResponse;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MemberServiceTests {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;
    private Long memberId;

    @BeforeEach
    void setUp(){
        Member member = memberRepository.save(Member.builder()
                .name("윤서준")
                .email("SeojunYoon@kosa.or.kr")
                .age(10).build());
        memberId = member.getId();
    }

    @Test
    void testFindById() {
        MemberResponse response = memberService.findById(1L);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("윤서준");
    }
}
