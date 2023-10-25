package com.wanted.teamr.skeleton.repository;

import com.wanted.teamr.skeleton.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
