package com.example.demo.service;


import com.example.demo.dto.MemberDto;
import com.example.demo.dto.MemberForm;
import com.example.demo.model.Member;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberDto findById(Long id){
        return memberRepository.findById(id)
                .map(this::mapToMemberDto)
                .orElseThrow();
    }

    private MemberDto mapToMemberDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail()).build();
    }

    public MemberDto create(MemberForm memberForm){

        Member member = Member.builder()
                .name(memberForm.getName())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .email(memberForm.getEmail())
                .build();

        memberRepository.save(member);
        return mapToMemberDto(member);
    }
    public Optional<MemberDto> findByEmail(String email){
        return memberRepository.findByEmail(email)
                .map(this::mapToMemberDto);
    }

    public boolean checkPassword(Long id, String password){

        Member member = memberRepository.findById(id).orElseThrow();
        return passwordEncoder.matches(password, member.getPassword());
    }

    public void updatePassword(Long id, String password){
        Member member = memberRepository.findById(id).orElseThrow();
        member.setPassword(passwordEncoder.encode(password));
        memberRepository.save(member);
    }

    public Page<MemberDto> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable)
                .map(this::mapToMemberDto);
    }

    public MemberDto patch(MemberForm memberForm) {
        Member member = memberRepository
                .findById(memberForm.getId())
                .orElseThrow();

        if (memberForm.getName() !=null){
            member.setName(memberForm.getName());
        }

        if (memberForm.getPassword() !=null) {
            member.setPassword(passwordEncoder.encode(memberForm.getPassword()));
        }

        if (memberForm.getEmail() !=null) {
            member.setEmail(memberForm.getEmail());
        }

        memberRepository.save(member);

        return mapToMemberDto(member);
    }

    public void deleteById(Long id){

        Member member = memberRepository
                .findById(id)
                .orElseThrow();

        articleRepository.deleteAllByMember(member);
        memberRepository.delete(member);
    }
}
