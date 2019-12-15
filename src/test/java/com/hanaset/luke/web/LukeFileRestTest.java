package com.hanaset.luke.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaset.luke.service.LukeFileService;
import com.hanaset.luke.web.rest.LukeFileRest;
import com.hanaset.luke.web.rest.advice.LukeApiRestAdvice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LukeFileRestTest {

    @Mock
    private LukeFileService lukeFileService;

    @InjectMocks
    private LukeFileRest lukeFileRest;

    private MockMvc mockMvc;
    private MockMultipartFile multipartFile;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lukeFileRest)
                .setControllerAdvice(new LukeApiRestAdvice())
                .build();

        String data = "연도,월,카카오페이\n" +
                "2005,1,1019\n" +
                "2005,2,1144\n" +
                "2005,3,1828";

        multipartFile = new MockMultipartFile("files", "사전과제3.csv", "text/plain", data.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void FileRest_UploadCSVFILE_테스트() throws Exception {

        given(lukeFileService.uploadFileData(any()))
                .willReturn(multipartFile.getOriginalFilename() + " 을 정상적으로 업로드 하였습니다.");

        final ResultActions resultActions = requestFileUpLoad(multipartFile);

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("0")))
                .andExpect(jsonPath("$.data",is(multipartFile.getOriginalFilename() + " 을 정상적으로 업로드 하였습니다.")));

    }

    private ResultActions requestFileUpLoad(MockMultipartFile multipartFile) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.multipart("/file")
                .file("file", multipartFile.getBytes()))
                .andDo(print());
    }
}
