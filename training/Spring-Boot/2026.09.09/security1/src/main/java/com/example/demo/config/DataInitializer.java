package com.example.demo.config;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        var passwordEncoder = new BCryptPasswordEncoder();
        memberRepository.save(Member.builder()
                .name("윤서준")
                .email("SeojunYoon@kosa.or.kr")
                .age(10)
                .password(passwordEncoder.encode("password"))
                .authority("ROLE_USER").build());

        memberRepository.save(Member.builder()
                .name("윤광철")
                .email("KwangcheolYoon@hanbit.or.kr")
                .age(50)
                .password(passwordEncoder.encode("password"))
                .authority("ROLE_ADMIN").build());
    }
}
