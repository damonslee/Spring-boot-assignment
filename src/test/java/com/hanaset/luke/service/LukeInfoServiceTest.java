package com.hanaset.luke.service;

import com.hanaset.luke.entity.BankEntity;
import com.hanaset.luke.entity.BankHistoryEntity;
import com.hanaset.luke.model.BankInfo;
import com.hanaset.luke.model.response.SupportAmount;
import com.hanaset.luke.model.response.YearInfo;
import com.hanaset.luke.model.response.YearMaxInfo;
import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("local")
public class LukeInfoServiceTest {

    @InjectMocks
    private LukeInfoService lukeInfoService;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private BankHistoryRepository bankHistoryRepository;

    private List<BankEntity> bankEntities = Lists.newArrayList();
    private List<BankHistoryEntity> bankHistoryEntities = Lists.newArrayList();

    @Before
    public void setUp() {
        bankEntities.add(BankEntity.builder().instituteCode("bnk1").instituteName("카카오페이").build());
        bankEntities.add(BankEntity.builder().instituteCode("bnk2").instituteName("국민은행").build());
        bankEntities.add(BankEntity.builder().instituteCode("bnk3").instituteName("외환은행").build());

        for (int i = 1; i <= 12; i++) {
            bankHistoryEntities.add(BankHistoryEntity.builder().id(Long.valueOf(i)).bankCode("bnk1").price(500L).year(2005L).month(Long.valueOf(i)).build());
            bankHistoryEntities.add(BankHistoryEntity.builder().id(Long.valueOf(i)).bankCode("bnk2").price(1000L).year(2005L).month(Long.valueOf(i)).build());
            bankHistoryEntities.add(BankHistoryEntity.builder().id(Long.valueOf(i)).bankCode("bnk3").price(1500L).year(2005L).month(Long.valueOf(i)).build());
        }
    }

    @Test
    public void InfoService_GetBankInfoList_테스트() {

        final List<BankInfo> bankInfos = bankEntities.stream().map(bankEntity ->
                BankInfo.builder()
                        .code(bankEntity.getInstituteCode())
                        .name(bankEntity.getInstituteName())
                        .build())
                .collect(Collectors.toList());

        given(bankRepository.findAll()).willReturn(bankEntities);
        final List<BankInfo> getBankInfo = lukeInfoService.getBankInfoList();

        assertThat(getBankInfo, is(bankInfos));
    }

    @Test
    public void InfoService_GetEveryYearInfo_테스트() {

        given(bankHistoryRepository.getYearList()).willReturn(Lists.newArrayList(2005L));
        given(bankRepository.findAll()).willReturn(bankEntities);
        given(bankHistoryRepository.findByYearOrderByMonth(any())).willReturn(bankHistoryEntities);

        final List<YearInfo> yearInfos = lukeInfoService.getEveryYearInfo();

        assertThat(yearInfos.get(0).getYear(), is(2005L));
        assertThat(yearInfos.get(0).getTotalAmount(), is(36000L));
        assertThat(yearInfos.get(0).getDetailAmount().get("카카오페이"), is(6000L));
        assertThat(yearInfos.get(0).getDetailAmount().get("국민은행"), is(12000L));
        assertThat(yearInfos.get(0).getDetailAmount().get("외환은행"), is(18000L));

    }

    @Test
    public void InfoService_GetMaximumAmountYear_테스트() {

        given(bankHistoryRepository.getYearList()).willReturn(Lists.newArrayList(2005L));
        given(bankRepository.findAll()).willReturn(bankEntities);
        given(bankHistoryRepository.findByYearOrderByMonth(any())).willReturn(bankHistoryEntities);

        final List<YearMaxInfo> yearMaxInfos = lukeInfoService.getMaximumAmountYear();

        assertThat(yearMaxInfos.get(0).getYear(), is(2005L));
        assertThat(yearMaxInfos.get(0).getBank(), is("외환은행"));
    }

    @Test
    public void InfoService_GetExchangeBankInfo_테스트() {

        given(bankRepository.findByInstituteName(any())).willReturn(Optional.of(BankEntity.builder().instituteCode("bnk3").instituteName("외환은행").build()));
        given(bankHistoryRepository.findByBankCodeAndYearBetween("bnk3", 2005L, 2006L))
                .willReturn(bankHistoryEntities.stream()
                        .filter(bankHistoryEntity -> bankHistoryEntity.getBankCode() == "bnk3")
                        .collect(Collectors.toList()));

        final List<SupportAmount> supportAmounts = lukeInfoService.getExchangeBankInfo("외환은행", 2005L, 2006L);
        assertThat(supportAmounts.get(0).getYear(), is(2006L));
        assertThat(supportAmounts.get(1).getYear(), is(2005L));
    }

    @Test(expected = LukeApiRestException.class)
    public void InfoService_GetExchangeBankInfo_잘못된은행입력_테스트() {

        given(bankRepository.findByInstituteName(any())).willReturn(Optional.empty());

        final List<SupportAmount> supportAmounts = lukeInfoService.getExchangeBankInfo("외환은행!!", 2005L, 2006L);
    }

}
