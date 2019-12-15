package com.hanaset.luke.service;

import com.hanaset.luke.entity.BankEntity;
import com.hanaset.luke.entity.BankHistoryEntity;
import com.hanaset.luke.model.response.PredictionResponse;
import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("development")
public class LukePredictionServiceTest {

    @InjectMocks
    private LukePredictionService lukePredictionService;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private BankHistoryRepository bankHistoryRepository;

    private List<BankEntity> bankEntities = Lists.newArrayList();
    private List<BankHistoryEntity> bankHistoryEntities = Lists.newArrayList();

    @Before
    public void setUp() {
        bankEntities.add(BankEntity.builder().instituteCode("bnk1").instituteName("카카오페이").build());

        for (int i = 1; i <= 12; i++) {
            bankHistoryEntities.add(BankHistoryEntity.builder().id(Long.valueOf(i)).bankCode("bnk1").price(500L * i).year(2005L).month(Long.valueOf(i)).build());
        }
    }

    @Test
    public void PredictionService_PredictionBankData_테스트() {

        given(bankRepository.findByInstituteName(any())).willReturn(Optional.of(BankEntity.builder().instituteName("카카오페이").instituteCode("bnk1").build()));
        given(bankHistoryRepository.findByBankCodeOrderByYearAscMonthAsc(any())).willReturn(bankHistoryEntities);

        final PredictionResponse predictionResponse = lukePredictionService.predictionBankData("카카오페이", 2006L, 2L);

        assertThat(predictionResponse.getAmount(), is(7000L));

    }
}
