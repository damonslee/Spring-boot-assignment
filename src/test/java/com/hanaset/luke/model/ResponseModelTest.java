package com.hanaset.luke.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hanaset.luke.model.response.PredictionResponse;
import com.hanaset.luke.model.response.SupportAmount;
import com.hanaset.luke.model.response.YearInfo;
import com.hanaset.luke.model.response.YearMaxInfo;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ResponseModelTest {

    @Test
    public void PredictionResponse_빌드테스트() {

        final String bank = "bnk1";
        final Long year = 2018L;
        final Long month = 2L;
        final Long amount = 4960L;

        final PredictionResponse response = PredictionResponse.builder()
                .bank(bank)
                .year(year)
                .month(month)
                .amount(amount)
                .build();

        assertThat(response.getBank(), is(bank));
        assertThat(response.getYear(), is(year));
        assertThat(response.getMonth(), is(month));
        assertThat(response.getAmount(), is(amount));
    }

    @Test
    public void SupportAmount_빌드테스트() {

        final Long year = 2005L;
        final Long amount = 4950L;

        final SupportAmount supportAmount = SupportAmount.builder()
                .year(year)
                .amount(amount)
                .build();

        assertThat(supportAmount.getYear(), is(year));
        assertThat(supportAmount.getAmount(), is(amount));
    }

    @Test
    public void YearInfo_빌드테스트() {

        final Long year = 2016L;
        final Long totalAmount = 10000L;
        final Map<String, Long> detailAmount =
                Maps.newHashMap(ImmutableMap.of(
                        "국민은행", 1000L,
                        "신한은행", 2000L,
                        "하나은행", 3000L
                ));

        final YearInfo  yearInfo = YearInfo.builder()
                .year(year)
                .totalAmount(totalAmount)
                .detailAmount(detailAmount)
                .build();

        assertThat(yearInfo.getYear(), is(year));
        assertThat(yearInfo.getTotalAmount(), is(totalAmount));
        assertThat(yearInfo.getDetailAmount(), is(detailAmount));
    }

    @Test
    public void YearMaxInfo_빌드테스트() {

        final Long year = 2006L;
        final String bank = "신한은행";

        final YearMaxInfo yearMaxInfo = YearMaxInfo.builder()
                .year(year)
                .bank(bank)
                .build();

        assertThat(yearMaxInfo.getYear(), is(year));
        assertThat(yearMaxInfo.getBank(), is(bank));
    }
}
