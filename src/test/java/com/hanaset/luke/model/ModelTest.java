package com.hanaset.luke.model;

import org.junit.Test;

import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ModelTest {

    @Test
    public void BankInfo_빌드테스트() {
        final String code = "bnk1";
        final String name = "국민은행";

        final BankInfo bankInfo = BankInfo.builder()
                .code(code)
                .name(name)
                .build();

        assertThat(bankInfo.getCode(), is(code));
        assertThat(bankInfo.getName(), is(name));
    }

    @Test
    public void JwtToken_빌드테스트() {

        final String token = "ADFBZXFQWEDGFASDASDASDASDASD";
        final ZonedDateTime expiredTime = ZonedDateTime.now();
        final String expiredString = expiredTime.toString();

        final JwtToken jwtToken = JwtToken.builder()
                .token(token)
                .expiredTime(expiredTime)
                .expiredString(expiredString)
                .build();

        assertThat(jwtToken.getToken(), is(token));
        assertThat(jwtToken.getExpiredTime(), is(expiredTime));
        assertThat(jwtToken.getExpiredString(), is(expiredString));
    }

    @Test
    public void PredictionInfo_빌드테스트() {

        final Long year = 2018L;
        final Long month = 2L;
        final Long gap = 13L;
        final Double gapPercent = 1.3D;
        final Long price = 5000L;

        final PredictionInfo predictionInfo = PredictionInfo.builder()
                .year(year)
                .month(month)
                .gap(gap)
                .gapPercent(gapPercent)
                .price(price)
                .build();

        assertThat(predictionInfo.getYear(), is(year));
        assertThat(predictionInfo.getMonth(), is(month));
        assertThat(predictionInfo.getGap(), is(gap));
        assertThat(predictionInfo.getGapPercent(), is(gapPercent));
        assertThat(predictionInfo.getPrice(), is(price));
    }
}
