package com.hanaset.luke.service;

import com.hanaset.luke.entity.UserEntity;
import com.hanaset.luke.model.JwtToken;
import com.hanaset.luke.model.request.SignRequest;
import com.hanaset.luke.repository.UserRepository;
import com.hanaset.luke.service.constants.JwtConstants;
import com.hanaset.luke.utils.SecurityUtil;
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
public class LukeAuthService {

    private final UserRepository userRepository;

    private final String SECRET_KEY = "KAKAO_PAY_TEST";

    public LukeAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JwtToken userSignUp(SignRequest request) {

        if (userRepository.existsByUserId(request.getId())) {
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "중복되는 ID입니다.");
        }

        JwtToken token = create("user_id", request.getId(), "token", JwtConstants.expiredTime);

        userRepository.save(UserEntity.builder()
                .userId(request.getId())
                .password(SecurityUtil.sha256(request.getPassword()))
                .build());

        return token;
    }

    public JwtToken userSignIn(SignRequest request) {

        UserEntity entity = userRepository.findByUserIdAndPassword(request.getId(), SecurityUtil.sha256(request.getPassword())).orElseThrow(() -> new LukeApiRestException(ErrorCode.SIGN_IN_FAILED, "ID 또는 Password가 일치하지 않습니다."));
        entity.setLastLogin(ZonedDateTime.now());
        entity.setUpdDtime(ZonedDateTime.now());
        userRepository.save(entity);
        return create("user_id", request.getId(), "token", JwtConstants.expiredTime);
    }

    public JwtToken userRefreshToken(String token) {
        String[] tokenArray = token.split(" ");
        if (!tokenArray[0].equals("Bearer")) {
            throw new LukeApiRestException(ErrorCode.REFRESH_TOKEN_ERROR, "Authorization [Bearer {token}]의 형식이 맞지 않습니다.");
        }

        String userId = validAndParsingToken(tokenArray[1]).getBody().get("user_id").toString();
        return create("user_id", userId, "user", JwtConstants.expiredTime);
    }

    public <T> JwtToken create(String key, T data, String subject, Long expire) {

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + expire);
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(expireTime.toInstant(), ZoneId.of("Asia/Seoul"));
        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("regDate", System.currentTimeMillis())
                .setExpiration(expireTime)
                .setSubject(subject)
                .claim(key, data)
                .signWith(SignatureAlgorithm.HS256, generateKey())
                .compact();

        return JwtToken.builder()
                .token(jwt)
                .expiredTime(zonedDateTime)
                .expiredString(zonedDateTime.toString())
                .build();
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
