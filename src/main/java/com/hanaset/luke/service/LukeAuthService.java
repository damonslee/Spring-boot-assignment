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

        validUserId(request.getId()); // 영어, 숫자 외에 단어를 체크
        validPassword(request.getPassword()); // 영어, 숫자, 특수문자(!@#$%^&*)외에 단어를 체크
        JwtToken token = jwtService.create("user_id", request.getId(), "token");

        userRepository.save(UserEntity.builder()
                .userId(request.getId())
                .password(SecurityUtil.sha256(request.getPassword()))
                .build());

        return token;
    }

    public void validUserId(String userId) {

        Pattern pattern = Pattern.compile("[^0-9a-zA-Z]");

        if (pattern.matcher(userId).find() == true) {
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "ID에는 숫자, 영소대문자만 가능합니다.");
        }

        if (userId.length() > 16) {
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "ID는 최대 16자까지 가능합니다.");
        }

        if (userRepository.findByUserId(userId).orElse(null) != null) {
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "중복되는 ID입니다.");
        }
    }

    public void validPassword(String password) {

        Pattern pattern = Pattern.compile("[^0-9a-zA-Z!@#$%^&*]");

        if (pattern.matcher(password).find() == true) {
//            log.error("Password에는 숫자, 영소대문자, 특수문자(!@#$%^&*)만 가능합니다.");
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "Password에는 숫자, 영소대문자, 특수문자(!@#$%^&*)만 가능합니다.");
        }

        if (password.length() > 32) {
//            log.error("Password는 최대 32자까지 가능합니다.");
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "Password는 최대 32자까지 가능합니다.");
        } else if (password.length() < 8) {
//            log.error("Password는 최소 8자리입니다.");
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "Password는 최소 8자리입니다.");
        }
    }

    public JwtToken userSignIn(SignRequest request) {

        userRepository.findByUserIdAndPassword(request.getId(), SecurityUtil.sha256(request.getPassword())).orElseThrow(() -> new LukeApiRestException(ErrorCode.SIGN_IN_FAILED, "ID 또는 Password가 일치하지 않습니다."));

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
