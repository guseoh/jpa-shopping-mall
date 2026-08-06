package com.example.jpastudy.member.repository;

import com.example.jpastudy.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
