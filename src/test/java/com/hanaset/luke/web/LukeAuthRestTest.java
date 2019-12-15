package com.hanaset.luke.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaset.luke.model.JwtToken;
import com.hanaset.luke.model.request.SignRequest;
import com.hanaset.luke.service.LukeAuthService;
import com.hanaset.luke.web.rest.LukeAuthRest;
import com.hanaset.luke.web.rest.advice.LukeApiRestAdvice;
import com.hanaset.luke.web.rest.exception.ErrorCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LukeAuthRestTest {

    @Mock
    private LukeAuthService lukeAuthService;

    @InjectMocks
    private LukeAuthRest lukeAuthRest;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lukeAuthRest)
                .setControllerAdvice(new LukeApiRestAdvice())
                .build();
    }

    @Test
    public void AuthRest_SignUp_테스트() throws Exception {

        //given
        final SignRequest request = SignRequest.builder()
                .id("kakaopay")
                .password("kakaopay1234@!")
                .build();

        final JwtToken jwtToken = JwtToken.builder()
                .token("test_Token")
                .expiredTime(ZonedDateTime.now())
                .expiredString(ZonedDateTime.now().toString())
                .build();

        given(lukeAuthService.userSignUp(any()))
                .willReturn(jwtToken);

        final ResultActions resultActions = requestSignUp(request);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.data.token", is(jwtToken.getToken())))
                .andExpect(jsonPath("$.data.expired_string", is(jwtToken.getExpiredString())));

    }

    @Test
    public void AuthRest_SignUp_ID특수문자포함_테스트() throws Exception {

        //given
        final SignRequest request = SignRequest.builder()
                .id("kakaopay!@#$")
                .password("kakaopay1234@!")
                .build();


        final ResultActions resultActions = requestSignUp(request);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.REQUEST_ERROR)));

    }

    @Test
    public void AuthRest_SignUp_ID16자리이상_테스트() throws Exception {

        //given
        final SignRequest request = SignRequest.builder()
                .id("kakaopay1234567890")
                .password("kakaopay1234@!")
                .build();


        final ResultActions resultActions = requestSignUp(request);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.REQUEST_ERROR)));

    }

    @Test
    public void AuthRest_SignUp_PASSWORD제외문자포함_테스트() throws Exception {

        //given
        final SignRequest request = SignRequest.builder()
                .id("kakaopay")
                .password("kakaopay1234@!{}()")
                .build();


        final ResultActions resultActions = requestSignUp(request);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.REQUEST_ERROR)));
    }

    @Test
    public void AuthRest_SignUp_PASSWORD10자리미만_테스트() throws Exception {

        //given
        final SignRequest request = SignRequest.builder()
                .id("kakaopay")
                .password("123456")
                .build();


        final ResultActions resultActions = requestSignUp(request);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.REQUEST_ERROR)));
    }

    @Test
    public void AuthRest_SignUp_PASSWORD50자리이상_테스트() throws Exception {

        //given
        final SignRequest request = SignRequest.builder()
                .id("kakaopay")
                .password("1234567890123456789012345678901234567890123456789012345678901234567890")
                .build();


        final ResultActions resultActions = requestSignUp(request);

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCode.REQUEST_ERROR)));
    }


    @Test
    public void AuthRest_SignIn_테스트() throws Exception {

        //given
        final SignRequest request = SignRequest.builder()
                .id("kakaopay")
                .password("kakaopay1234@!")
                .build();

        final JwtToken jwtToken = JwtToken.builder()
                .token("test_Token")
                .expiredTime(ZonedDateTime.now())
                .expiredString(ZonedDateTime.now().toString())
                .build();

        given(lukeAuthService.userSignIn(any()))
                .willReturn(jwtToken);

        final ResultActions resultActions = requestSignIn(request);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.data.token", is(jwtToken.getToken())))
                .andExpect(jsonPath("$.data.expired_string", is(jwtToken.getExpiredString())));

    }


    @Test
    public void AuthRest_Refresh_테스트() throws Exception {

        final String token = "test_token";
        final JwtToken jwtToken = JwtToken.builder()
                .token("test_refresh_token")
                .expiredTime(ZonedDateTime.now())
                .expiredString(ZonedDateTime.now().toString())
                .build();

        given(lukeAuthService.userRefreshToken(any()))
                .willReturn(jwtToken);

        final ResultActions resultActions = requestRefresh(token);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.data.token", is(jwtToken.getToken())))
                .andExpect(jsonPath("$.data.expired_string", is(jwtToken.getExpiredString())));

    }

    private ResultActions requestSignUp(SignRequest request) throws Exception {
        return mockMvc.perform(post("/auth/sign_up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print());
    }

    private ResultActions requestSignIn(SignRequest request) throws Exception {
        return mockMvc.perform(post("/auth/sign_in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print());
    }

    private ResultActions requestRefresh(String token) throws Exception {
        return mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andDo(print());
    }
}
