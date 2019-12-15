package com.hanaset.luke;

import com.hanaset.luke.model.JwtToken;
import com.hanaset.luke.model.request.SignRequest;
import com.hanaset.luke.repository.UserRepository;
import com.hanaset.luke.service.LukeAuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@SpringBootApplication
@ContextConfiguration(classes = {
        UserRepository.class
})
@Transactional
public class LukeAuthTest {

    @Autowired
    private LukeAuthService lukeAuthService;

    @Test
    public void authSignUpTest() {
        System.out.println(lukeAuthService.userSignUp(SignRequest.builder().id("abasdfadsfadfadfadfadfadfasdfasdfasdf123123123123").password("test1234").build()));
    }

    @Test
    public void authSingInTest() {
        System.out.println(lukeAuthService.userSignIn(SignRequest.builder().id("test").password("test1234").build()));
    }

    @Test
    public void authCreateJwtTest() {
        JwtToken token = lukeAuthService.create("user_id", "b183523", "user", System.currentTimeMillis() + 1000 * 60 * 60);
        System.out.println(lukeAuthService.validAndParsingToken(token.getToken()));
    }

    @Test
    public void authRefreshJwtTest() {
        JwtToken token = lukeAuthService.create("user_id", "b183523", "user", System.currentTimeMillis() + 1000 * 60 * 60);
        String request = "Bearer " + token.getToken();
        System.out.println(lukeAuthService.userRefreshToken(request));
    }
}
