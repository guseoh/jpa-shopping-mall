package com.example.jpastudy.member.service;

import com.example.jpastudy.member.domain.Member;
import com.example.jpastudy.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void changeName(Long memberId, String newName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow();

        member.changeName(newName);

    }
}
