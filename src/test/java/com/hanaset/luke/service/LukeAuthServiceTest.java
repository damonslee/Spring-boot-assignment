package com.hanaset.luke.service;

import com.hanaset.luke.entity.UserEntity;
import com.hanaset.luke.model.JwtToken;
import com.hanaset.luke.model.request.SignRequest;
import com.hanaset.luke.repository.UserRepository;
import com.hanaset.luke.service.constants.JwtConstants;
import com.hanaset.luke.utils.SecurityUtil;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("development")
public class LukeAuthServiceTest {

    @InjectMocks
    private LukeAuthService lukeAuthService;

    @Mock
    private UserRepository userRepository;

    private SignRequest signRequest;
    private UserEntity userEntity;

    @Before
    public void setUp() {
        signRequest = SignRequest.builder()
                .id("kakaopay001")
                .password("kakaopassword1!")
                .build();

        userEntity = UserEntity.builder()
                .userId(signRequest.getId())
                .password(SecurityUtil.sha256(signRequest.getPassword()))
                .build();
    }

    @Test
    public void AuthService_JWTCreateAndValid_테스트() {

        final String id = "kakaopay001";
        final JwtToken token = lukeAuthService.create("user_id", id, "user", JwtConstants.expiredTime);

        final Jws<Claims> claimsJws = lukeAuthService.validAndParsingToken(token.getToken());
        assertThat(claimsJws.getBody().get("user_id"), is(id));
    }

    @Test(expected = LukeApiRestException.class)
    public void AuthService_JWT_토큰만기_테스트() {

        final String id = "kakaopay001";
        final JwtToken token = lukeAuthService.create("user_id", id, "user", 1000 * 3L);

        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {

        }
        final Jws<Claims> claimsJws = lukeAuthService.validAndParsingToken(token.getToken());
    }

    @Test
    public void AuthService_SignUp_테스트() {

        final JwtToken jwtToken = lukeAuthService.userSignUp(signRequest);

        assertThat(lukeAuthService.validAndParsingToken(jwtToken.getToken()).getBody().get("user_id"), is(signRequest.getId()));

    }

    @Test(expected = LukeApiRestException.class)
    public void AuthService_SignUp_아이디중복_테스트() {

        given(userRepository.existsByUserId(any())).willReturn(true);
        final JwtToken jwtToken = lukeAuthService.userSignUp(signRequest);
    }

    @Test
    public void AuthService_SignIn_테스트() {

        given(userRepository.findByUserIdAndPassword(any(), any())).willReturn(Optional.of(userEntity));
        final JwtToken jwtToken = lukeAuthService.userSignIn(signRequest);

        assertThat(lukeAuthService.validAndParsingToken(jwtToken.getToken()).getBody().get("user_id"), is(signRequest.getId()));
    }

    @Test(expected = LukeApiRestException.class)
    public void AuthService_SignIn_비회원_테스트() {

        given(userRepository.findByUserIdAndPassword(any(), any())).willReturn(Optional.empty());
        final JwtToken jwtToken = lukeAuthService.userSignIn(signRequest);

        assertThat(lukeAuthService.validAndParsingToken(jwtToken.getToken()).getBody().get("user_id"), is(signRequest.getId()));
    }

    @Test
    public void AuthService_RefreshToken_테스트() {

        given(userRepository.findByUserIdAndPassword(any(), any())).willReturn(Optional.of(userEntity));
        final JwtToken jwtToken = lukeAuthService.userSignIn(signRequest);
        final JwtToken refreshToken = lukeAuthService.userRefreshToken("Bearer " + jwtToken.getToken());

        assertThat(lukeAuthService.validAndParsingToken(jwtToken.getToken()).getBody().get("user_id"),
                is(lukeAuthService.validAndParsingToken(refreshToken.getToken()).getBody().get("user_id")));
    }

    @Test(expected = LukeApiRestException.class)
    public void AuthService_RefreshToken_요청형식불일치_테스트() {

        given(userRepository.findByUserIdAndPassword(any(), any())).willReturn(Optional.of(userEntity));
        final JwtToken jwtToken = lukeAuthService.userSignIn(signRequest);
        final JwtToken refreshToken = lukeAuthService.userRefreshToken(jwtToken.getToken());

        assertThat(lukeAuthService.validAndParsingToken(jwtToken.getToken()).getBody().get("user_id"),
                is(lukeAuthService.validAndParsingToken(refreshToken.getToken()).getBody().get("user_id")));
    }

    @Test(expected = LukeApiRestException.class)
    public void AuthService_RefreshToke_인증실패_테스트() {

        final JwtToken jwtToken = JwtToken.builder().token("ABCADFALOIDJLKJNEKN_BVOIDFLIJ_ILJD").build();
        final JwtToken refreshToken = lukeAuthService.userRefreshToken("Bearer " + jwtToken.getToken());

        assertThat(lukeAuthService.validAndParsingToken(jwtToken.getToken()).getBody().get("user_id"),
                is(lukeAuthService.validAndParsingToken(refreshToken.getToken()).getBody().get("user_id")));

    }

}
