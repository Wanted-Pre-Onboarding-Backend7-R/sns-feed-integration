package com.wanted.teamr.snsfeedintegration.jwt;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.domain.RefreshToken;
import com.wanted.teamr.snsfeedintegration.domain.UserDetailsImpl;
import com.wanted.teamr.snsfeedintegration.dto.TokenDto;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

/**
 * JWT 토큰에 관련된 암호화, 복호화, 검증 로직
 */
@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
    private final Key key;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * application.yml에서 주입받은 secret 값을 복호화하여 key 변수에 할당
     */
    public TokenProvider(@Value("${jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Member member) {
        long now = (new Date().getTime());

        /* Access Token 생성 */
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(member.getAccountName())
                .claim(AUTHORITIES_KEY, member.getAuthority())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        /* Refresh Token 생성 */
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        RefreshToken refreshTokenObject = RefreshToken.of(member, refreshToken);
        refreshTokenRepository.save(refreshTokenObject);

        return TokenDto.of(BEARER_TYPE, accessToken, refreshToken, accessTokenExpiresIn.getTime());
    }

    /**
     * 스프링 시큐리티 -> 컨텍스트 홀더 안에 저장된 회원 정보 확인
     */
    public Member getMemberFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return null;
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getMember();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info(ErrorCode.MALFORMED_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            log.info(ErrorCode.EXPIRED_TOKEN.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info(ErrorCode.WRONG_TOKEN.getMessage());
        } catch (IllegalArgumentException e) {
            log.info(ErrorCode.ILLEGAL_TOKEN.getMessage());
        }
        return false;
    }

    @Transactional(readOnly = true)
    public RefreshToken isPresentRefreshToken(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByMember(member);
        return optionalRefreshToken.orElse(null);
    }

    @Transactional
    public ResponseEntity<?> deleteRefreshToken(Member member) {
        RefreshToken refreshToken = isPresentRefreshToken(member);
        if (null == refreshToken) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        refreshTokenRepository.delete(refreshToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Key getKey() {
        return key;
    }

}
