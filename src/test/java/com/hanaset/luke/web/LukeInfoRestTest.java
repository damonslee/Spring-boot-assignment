package com.hanaset.luke.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaset.luke.model.request.PredictionRequest;
import com.hanaset.luke.model.response.PredictionResponse;
import com.hanaset.luke.service.LukeInfoService;
import com.hanaset.luke.service.LukePredictionService;
import com.hanaset.luke.web.rest.LukeInfoRest;
import com.hanaset.luke.web.rest.advice.LukeApiRestAdvice;
import org.assertj.core.util.Lists;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LukeInfoRestTest {

    @Mock
    private LukePredictionService lukePredictionService;

    @Mock
    private LukeInfoService lukeInfoService;

    @InjectMocks
    private LukeInfoRest lukeInfoRest;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private PredictionRequest request;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lukeInfoRest)
                .setControllerAdvice(new LukeApiRestAdvice())
                .build();

        request = PredictionRequest.builder()
                .bank("카카오페이")
                .month(2L)
                .build();
    }

    @Test
    public void InfoRest_GetBankList_테스트() throws Exception {

        given(lukeInfoService.getBankInfoList()).willReturn(Lists.emptyList());

        final ResultActions resultActions = requestGetBankList();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("0")));
    }

    @Test
    public void InfoRest_GetEveryYearInfo_테스트() throws Exception {

        given(lukeInfoService.getEveryYearInfo()).willReturn(Lists.emptyList());

        final ResultActions resultActions = requestGetEveryYearInfo();

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    public void InfoRest_GetMaximumAmountYear_테스트()  throws Exception {

        given(lukeInfoService.getMaximumAmountYear()).willReturn(Lists.emptyList());

        final ResultActions resultActions = requestGetMaximumAmountYear();

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    public void InfoRest_GetMaxMinInfo_테스트() throws Exception {

        given(lukeInfoService.getExchangeBankInfo(any(), any(), any())).willReturn(Lists.emptyList());

        final ResultActions resultActions = requestGetMaxMinInfo();

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bank", is("외환은행")));
    }

    @Test
    public void InfoRest_PredictionByBank_테스트() throws Exception {

        given(lukePredictionService.predictionBankData(any(), any(), any())).willReturn(PredictionResponse.builder().build());

        final ResultActions resultActions = requestPredictionByBank(request);

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    public void InfoRest_PredictionByBank_유효하지않은달_테스트() throws Exception {

        given(lukePredictionService.predictionBankData(any(), any(), any())).willReturn(PredictionResponse.builder().build());
        request.setMonth(13L);
        final ResultActions resultActions = requestPredictionByBank(request);

        resultActions
                .andExpect(status().isBadRequest());

    }

    private ResultActions requestGetBankList() throws Exception {
        return mockMvc.perform(get("/info/bank_list")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestGetEveryYearInfo() throws Exception {
        return mockMvc.perform(get("/info/every_year")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestGetMaximumAmountYear() throws Exception {
        return mockMvc.perform(get("/info/maximum_amount_year")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestGetMaxMinInfo() throws Exception {
        return mockMvc.perform(get("/info/history_max_min")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestPredictionByBank(PredictionRequest request) throws Exception {
        return mockMvc.perform(post("/info/prediction")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print());
    }
}
