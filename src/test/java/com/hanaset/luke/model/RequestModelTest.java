package com.hanaset.luke.model;

import com.hanaset.luke.model.request.PredictionRequest;
import com.hanaset.luke.model.request.SignRequest;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RequestModelTest {

    @Test
    public void SignRequest_빌드테스트() {

        final String id = "aA1bB2cC3";
        final String password = "abcdABCD1234!@#$";

        final SignRequest request = SignRequest.builder()
                .id(id)
                .password(password)
                .build();

        assertThat(request.getId(), is(id));
        assertThat(request.getPassword(), is(password));
    }

    @Test
    public void PredictionRequest_빌드테스트() {

        final String bank = "국민은행";
        final Long month = 2L;

        final PredictionRequest request = PredictionRequest.builder()
                .bank(bank)
                .month(month)
                .build();

        assertThat(request.getBank(), is(bank));
        assertThat(request.getMonth(), is(month));
    }
}
