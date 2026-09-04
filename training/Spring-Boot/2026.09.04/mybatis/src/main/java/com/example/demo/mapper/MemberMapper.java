package com.example.demo.mapper;

import com.example.demo.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    List<Member> selectAll();

    //SELECT * FORM member WHERE id = #(id);
    Member selectById(@Param("id") Long id);

    // 1. 이름에 특정 문자가 포함된 회원 조회
    List<Member> selectByName(@Param("name") String name);

    // 2. 새로운 회원 추가
    int insertMember(Member member);
}
