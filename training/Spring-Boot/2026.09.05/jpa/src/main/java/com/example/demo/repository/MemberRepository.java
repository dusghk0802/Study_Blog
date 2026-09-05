package com.example.demo.repository;

import com.example.demo.model.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);

    List<Member> findByEmail(String email);

    List<Member> findByAgeGreaterThan(Integer age);

    //이메일을 이용한 회원 삭제
    @Transactional
    int deleteByEmail(String email);

    //이름을 이용해서 회원 삭제
    @Transactional
    int deleteByName(String name);
}
