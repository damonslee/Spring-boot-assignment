package com.hanaset.luke.service;

import com.hanaset.luke.entitiy.UserEntity;
import com.hanaset.luke.model.JwtToken;
import com.hanaset.luke.model.request.SignRequest;
import com.hanaset.luke.repository.UserRepository;
import com.hanaset.luke.utils.SecurityUtil;
import com.hanaset.luke.web.rest.exception.ErrorCode;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.regex.Pattern;

@Slf4j
@Service
public class LukeAuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LukeAuthService(UserRepository userRepository,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public JwtToken userSignUp(SignRequest request) {

        if (userRepository.findByUserId(request.getId()).orElse(null) != null) {
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "중복되는 ID입니다.");
        }

        JwtToken token = jwtService.create("user_id", request.getId(), "token");

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
        return jwtService.create("user_id", request.getId(), "token");
    }

    public JwtToken userRefreshToken(String token) {
        String[] tokenArray = token.split(" ");
        if(!tokenArray[0].equals("Bearer")) {
            throw new LukeApiRestException(ErrorCode.REFRESH_TOKEN_ERROR, "Authorization [Bearer {token}]의 형식이 맞지 않습니다.");
        }

        String userId = jwtService.validAndParsingToken(tokenArray[1]).getBody().get("user_id").toString();
        return jwtService.create("user_id", userId, "user");
    }

}
