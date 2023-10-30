package com.wanted.teamr.snsfeedintegration.repository;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);

}
