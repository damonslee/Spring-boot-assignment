//package com.hanaset.luke;
//
//import com.hanaset.luke.repository.BankHistoryRepository;
//import com.hanaset.luke.repository.BankRepository;
//import com.hanaset.luke.service.LukeInfoService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("local")
//@SpringBootApplication
//@ContextConfiguration(classes = {
//        BankHistoryRepository.class,
//        BankRepository.class
//})
//public class LukeInfoServiceTest {
//
//    @Autowired
//    private LukeInfoService lukeInfoService;
//
//    @Test
//    public void infoServiceGetBankListTest() {
//        System.out.println(lukeInfoService.getBankInfoList());
//    }
//
//    @Test
//    public void infoServiceGetBankListTesticeGetYearTest() {
//        System.out.println(lukeInfoService.getEveryYearInfo());
//    }
//
//    @Test
//    public void infoServiceGetMaxiumAmountYearTest() {
//        System.out.println(lukeInfoService.getMaximumAmountYear());
//    }
//
//    @Test
//    public void infoServiceGetExchangeBankTest() {
//        System.out.println(lukeInfoService.getExchangeBankInfo("외환은행", 2005L, 2016L));
//    }
//}
