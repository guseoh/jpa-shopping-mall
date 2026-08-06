package com.example.jpastudy.member.service;

import com.example.jpastudy.member.domain.Member;
import com.example.jpastudy.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("서비스에서 회원 이름을 변경하면 변경 감지로 반영된다")
    void changeName() {

        Member member = memberRepository.save(
                new Member("홍길동", "service-update@example.com")
        );

        memberService.changeName(member.getId(), "김길동");

        entityManager.flush();
        entityManager.clear();

        Member foundMember = memberRepository.findById(member.getId())
                .orElseThrow();

        assertThat(foundMember.getName()).isEqualTo("김길동");
    }
}