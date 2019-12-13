package com.hanaset.luke;

import com.hanaset.luke.model.JwtToken;
import com.hanaset.luke.model.request.SignRequest;
import com.hanaset.luke.repository.UserRepository;
import com.hanaset.luke.service.JwtService;
import com.hanaset.luke.service.LukeAuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@SpringBootApplication
@ContextConfiguration(classes = {
        UserRepository.class
})
public class LukeAuthServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private LukeAuthService lukeAuthService;

    @Test
    public void authSignUpTest() {
        System.out.println(lukeAuthService.userSignUp(SignRequest.builder().id("test").password("test1234").build()));
    }

    @Test
    public void authSingInTest() {
        System.out.println(lukeAuthService.userSignIn(SignRequest.builder().id("test").password("test1234").build()));
    }

    @Test
    public void authCreateJwtTest() {
        JwtToken token = jwtService.create("user_id", "b183523", "user");
        System.out.println(jwtService.validAndParsingToken(token.getToken()));
    }

    @Test
    public void authRefreshJwtTest() {
        JwtToken token = jwtService.create("user_id", "b183523", "user");
        String request = "Bearer " + token.getToken();
        System.out.println(lukeAuthService.userRefreshToken(request));
    }
}
