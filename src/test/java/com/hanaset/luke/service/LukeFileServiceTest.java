package com.hanaset.luke.service;

import com.hanaset.luke.entity.BankEntity;
import com.hanaset.luke.entity.BankHistoryEntity;
import com.hanaset.luke.model.BankInfo;
import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("development")
@Transactional
public class LukeFileServiceTest {

    @Mock
    private BankRepository bankRepository;

    @Mock
    private BankHistoryRepository bankHistoryRepository;

    @InjectMocks
    private LukeFileService lukeFileService;

    private List<BankEntity> bankInfoList = Lists.newArrayList();
    private List<BankHistoryEntity> bankHistoryList = Lists.newArrayList();
    private MultipartFile multipartFile;

    @Before
    public void setUp() {
        bankInfoList.add(BankEntity.builder()
                .instituteName("카카오페이")
                .instituteCode("bnk10")
                .build());

        bankHistoryList.add(BankHistoryEntity.builder()
                .year(2019L)
                .month(12L)
                .price(5000L)
                .bankCode("bnk10")
                .build());

        String data = "연도,월,카카오페이\n" +
                "2005,1,1019\n" +
                "2005,2,1144\n" +
                "2005,3,1828";

        multipartFile = new MockMultipartFile("files", "사전과제3.csv", "text/plain", data.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void FileService_GetBankInfoList_테스트() {

        final List<BankInfo> bankInfos = bankInfoList.stream().map(bankEntity ->
                BankInfo.builder()
                        .code(bankEntity.getInstituteCode())
                        .name(bankEntity.getInstituteName())
                        .build())
                .collect(Collectors.toList());

        given(bankRepository.findAll()).willReturn(bankInfoList);
        final List<BankInfo> getBankInfo = lukeFileService.getBankInfoList();

        assertThat(getBankInfo, is(bankInfos));
    }

    @Test
    public void FileService_UploadData_테스트() {

        given(bankRepository.findAll()).willReturn(bankInfoList);
        final String response = lukeFileService.uploadFileData(multipartFile);

        assertTrue(response.contains("사전과제3.csv"));
    }

    @Test
    public void FileService_GetBankName_테스트() {

        final String bankName = "카카오페이1(억원)";
        final String result = lukeFileService.getBankName(bankName);

        assertThat(result, is("카카오페이"));
    }

    @Test
    public void FileService_UploadAndGetBankInfo_테스트() {

        final String[] banks = {"카카오페이", "신한은행", "국민은행"};
        final List<BankInfo> bankInfos = lukeFileService.uploadAndGetBankInfo(banks);

        for (int i = 0; i < banks.length; i++) {
            assertThat(banks[i], is(bankInfos.get(i).getName()));
        }
    }


}
