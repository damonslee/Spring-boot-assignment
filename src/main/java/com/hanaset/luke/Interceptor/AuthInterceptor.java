package com.hanaset.luke.Interceptor;

import com.hanaset.luke.service.JwtService;
import com.hanaset.luke.web.rest.exception.ErrorCode;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String HEADER_AUTH = "Authorization";
    private final JwtService jwtService;

    public AuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String token = request.getHeader(HEADER_AUTH);


        if (token != null) {
            jwtService.validAndParsingToken(token);
            return true;
        } else {
            throw new LukeApiRestException(ErrorCode.REQUEST_ERROR, "잘못된 요청입니다.");
        }
    }
}