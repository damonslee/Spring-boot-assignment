package com.hanaset.luke.service;

import com.hanaset.luke.model.JwtToken;
import com.hanaset.luke.web.rest.exception.ErrorCode;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    private final static String SECRET_KEY = "KAKAO_PAY_TEST";

    public <T> JwtToken create(String key, T data, String subject) {

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + (60 * 60 * 1000));
        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("regDate", System.currentTimeMillis())
                .setExpiration(expireTime)
                .setSubject(subject)
                .claim(key, data)
                .signWith(SignatureAlgorithm.HS256, generateKey())
                .compact();
        return JwtToken.builder().token(jwt).expireTime(ZonedDateTime.ofInstant(expireTime.toInstant(), ZoneId.of("Asia/Seoul"))).build();
    }

    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = SECRET_KEY.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("GenerateKey Exception : {}", e.getMessage());
            throw new LukeApiRestException(ErrorCode.TOKEN_CREATE_FAILED, "JWT 토큰 생성에 실패하였습니다.");
        }

        return key;
    }

    public Jws<Claims> validAndParsingToken(String jwt) {
        try {
            return Jwts.parser()
                    .setSigningKey(generateKey())
                    .parseClaimsJws(jwt);
        } catch (ExpiredJwtException e) {
            throw new LukeApiRestException(ErrorCode.EXPIRED_TOKEN_ERROR, "Auth Token이 만료되었습니다.");
        } catch (JwtException e) {
            throw new LukeApiRestException(ErrorCode.AUTH_TOKEN_FAILED, "Auth Token 인증에 실패하였습니다.");
        }
    }
}