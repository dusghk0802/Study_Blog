package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JdbcApplication implements ApplicationRunner {
//    private final  MemberRepository memberRepository;
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception{
/*        memberRepository.save(Member.builder()
                .name("정수빈").email("Jung@kosa.or.kr")
                .age(10).build());*/

/*        memberRepository.save(Member.builder()
                .name("윤정혁")
                .email("Yoon@kosa.co.kr")
                .age(10).build());*/

        //Update
/*        Member member = memberRepository.save(Member.builder()
                .name("윤정혁")
                .email("Yoon@kosa.co.kr")
                .age(10).build());*/

/*        var members = memberRepository.findAll();
        log.info("{}", members);*/

        /*member.setAge(11);
        memberRepository.save(member);
        log.info("{}", member);*/
        //똑같은 이름으로 중복되면 오류나서 오라클에서 테이블 삭제 후에 부트런 실행

        var members1 = memberRepository.findByName("윤서준");
        log.info("{}", members1);

        //나이가 20보다 큰 회원 조회
        var members2 = memberRepository.findByAgeGreaterThan(20);
        log.info("{}", members2);

        //회원 삭제
        memberRepository.deleteById(1L);
        log.info("회원 삭제 완료: id={}", 1L);

        /*1. 이순신회원을 save()로 저장한 결과를 member 변수에 받은 뒤,
                나이를 50세에서 35세로 수정하고 저장하세요.*/
/*        memberRepository.save(Member.builder()
                .name("이순신")
                        .email("Lee@kosa.co.kr")
                .age(50).build());*/

        memberRepository.deleteById(20L);
        log.info("회원 삭제 완료: id={}", 20L);

/*         Member member = memberRepository.save(Member.builder()
                .name("이순신")
                .email("Lee@kosa.co.kr")
                .age(35).build());*/

        /*2. 이메일이 "Yoon@kosa.or.kr"인 회원을 조회하는 코드를 작성하세요.*/
        var members3 = memberRepository.findByEmail("Yoon@kosa.or.kr");
        log.info("{}", members3);

        /*3. 이름이 "윤광철"이고 이메일이 "Kwang@hanbit.co.kr"인 회원을 조회하세요.
                두 조건이 모두 만족되어야 합니다.*/
        var members4 = memberRepository.findByNameAndEmail("윤광철", "Kwang@hanbit.co.kr");
        log.info("{}",members4);

        /*4. 이름이 "정수빈"인 회원을 조회한 뒤, 조회된 회원들을 반복하면서 id를 이용하여 삭제하는 코드를 작성하세요.*/
        var members5 = memberRepository.findByName("정수빈");
        log.info("{}",members5);

        memberRepository.deleteById(14L);
        log.info("회원 삭제 완료: id={}", 14L);
    }
}
