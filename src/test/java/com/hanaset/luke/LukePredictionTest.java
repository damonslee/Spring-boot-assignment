package com.hanaset.luke;

import com.hanaset.luke.repository.BankHistoryRepository;
import com.hanaset.luke.repository.BankRepository;
import com.hanaset.luke.service.LukePredictionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@SpringBootApplication
@ContextConfiguration(classes = {
        BankHistoryRepository.class,
        BankRepository.class
})
public class LukePredictionTest {
    @Autowired
    private LukePredictionService lukePredictionService;

    @Test
    public void predictionServiceBankDataTest() {
        lukePredictionService.predictionBankData("국민은행", 2018L, 2L);
    }

}
