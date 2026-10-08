package com.example.demo.service;

import com.example.demo.dto.MemberRequest;
import com.example.demo.dto.MemberResponse;
import com.example.demo.entity.Member;
import com.example.demo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public MemberResponse create(MemberRequest memberRequest){
        var member = Member.builder() //요청 DTO -> DB에 저장할 Entity로 변환
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .age(memberRequest.getAge())
                .enabled(true).build();
        memberRepository.save(member);

        //Entity를 클라이언트에게 보낼 Response DTO로 변환
        var memberResponse = MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge()).build();
        memberRepository.save(member);
        return mapToMemberResponse(member);
    }

    public List<MemberResponse> findAll(){
        return memberRepository.findAll().stream().map(this::mapToMemberResponse).toList();
    }

    @Transactional
    public List<MemberResponse> createBatch(List<MemberRequest> memberRequests){
        return memberRequests.stream().map(this::create).toList();
    }
    private MemberResponse mapToMemberResponse(Member member){
        return  MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .age(member.getAge()).build();
    }

    public MemberResponse findById(Long id){
        Member member = memberRepository.findById(id).orElseThrow();
        return mapToMemberResponse(member);
    }

    public Member save(Member member){
        return memberRepository.save(member);
    }

    public void deleteById(Long id){
        memberRepository.deleteById(id);
    }
}
