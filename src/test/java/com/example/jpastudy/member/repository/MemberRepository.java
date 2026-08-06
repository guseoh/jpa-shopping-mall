package com.example.jpastudy.member.repository;

import com.example.jpastudy.member.domain.Member;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("회원을 저장하고 같은 트랜잭션에서 조회한다")
    void saveAndFind() {
        Member member = new Member(
                "홍길동",
                "hong@example.com"
        );

        Member savedMember = memberRepository.save(member);

        Member foundMember = memberRepository.findById(savedMember.getId())
                .orElseThrow();

        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
        assertThat(foundMember.getName()).isEqualTo("홍길동");
        assertThat(foundMember.getEmail()).isEqualTo("hong@example.com");
    }

    @Test
    @DisplayName("영속성 컨텍스트를 비우면 DB에서 다시 조회한다")
    void saveAndFindAfterClear() {
        Member member = new Member(
                "김철수",
                "kim@example.com"
        );

        Member savedMember = memberRepository.save(member);
        Long memberId = savedMember.getId();

        entityManager.flush();
        entityManager.clear();

        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow();

        assertThat(foundMember).isNotSameAs(savedMember);
        assertThat(foundMember.getName()).isEqualTo("김철수");
    }


}