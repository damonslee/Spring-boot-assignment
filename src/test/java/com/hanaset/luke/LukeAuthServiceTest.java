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
    public void authSignInValidUserIdTest() {

//        lukeAuthService.validUserId("183523");
//        lukeAuthService.validUserId("hanaset");
//        lukeAuthService.validUserId("HANASET");
//        lukeAuthService.validUserId("하나셋");
//        lukeAuthService.validUserId("b183523!!!!");
        lukeAuthService.validUserId("Bb183523");
//        lukeAuthService.validUserId("Bb183523!!");
//        lukeAuthService.validUserId("Bb183523하나");
//        lukeAuthService.validUserId("1234567890123456789");
    }

    @Test
    public void authSignInValidPasswordTest() {

//        lukeAuthService.validPassword("123");
//        lukeAuthService.validPassword("1235678!@#");
//        lukeAuthService.validPassword("abcd5678!@#");
//        lukeAuthService.validPassword("ABCD678!@#");
//        lukeAuthService.validPassword("aBVD5678!@#~~ㄱ");
        lukeAuthService.validPassword("1234567890123456789012345678901234");
    }

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
