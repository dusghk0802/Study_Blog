package com.example.demo.repository;

import com.example.demo.model.Member;
import com.example.demo.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    List<Authority> findByMember(Member member);
}
