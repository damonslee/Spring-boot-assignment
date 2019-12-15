//package com.hanaset.luke.repository;
//
//import com.hanaset.luke.entity.BankEntity;
//import com.hanaset.luke.entity.UserEntity;
//import com.hanaset.luke.utils.SecurityUtil;
//import org.assertj.core.util.Lists;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//
//@RunWith(SpringRunner.class)
//@ActiveProfiles("local")
//@Transactional
//@DataJpaTest
//@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
//public class UserRepositoryTest {
//
//    @Autowired
//    private BankRepository bankRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Before
//    public void BankRepositorySetup() {
//        List<BankEntity> bankEntities = Lists.newArrayList();
//
//        bankEntities.add(BankEntity.builder()
//                .instituteCode("bnk10")
//                .instituteName("카카오페이")
//                .build());
//
//        bankEntities.add(BankEntity.builder()
//                .instituteCode("bnk11")
//                .instituteName("카카오페이2")
//                .build());
//
//        bankRepository.saveAll(bankEntities);
//    }
//
//    @Before
//    public void UserRepositorySetup() {
//        UserEntity userEntity = UserEntity.builder()
//                .userId("kakaopay001")
//                .password(SecurityUtil.sha256("kakaopayPassword1!"))
//                .build();
//
//        userRepository.save(userEntity);
//    }
//
//    @Test
//    public void BankRepository_findByInstituteCode_테스트() {
//
//        final String instituteCode = "bnk10";
//        final BankEntity bankEntity = bankRepository.findByInstituteCode(instituteCode).get();
//
//        assertThat(bankEntity.getInstituteCode(), is(instituteCode));
//        assertThat(bankEntity.getInstituteName(), is("카카오페이"));
//    }
//
//    @Test
//    public void BankRepository_findByInstituteName_테스트() {
//
//        final String instituteName = "카카오페이";
//        final BankEntity bankEntity = bankRepository.findByInstituteName(instituteName).get();
//
//        assertThat(bankEntity.getInstituteName(), is(instituteName));
//        assertThat(bankEntity.getInstituteCode(), is("bnk10"));
//    }
//
//    @Test
//    public void UserRepository_findByUserId_테스트() {
//
//        final String userId = "kakaopay001";
//        final UserEntity userEntity = userRepository.findByUserId(userId).get();
//
//        assertThat(userEntity.getUserId(), is(userId));
//    }
//
//    @Test
//    public void UserRepository_findByUserIdAndPassword_테스트() {
//
//        final String userId = "kakaopay001";
//        final String password = SecurityUtil.sha256("kakaopayPassword1!");
//
//        final UserEntity userEntity = userRepository.findByUserIdAndPassword(userId, password).get();
//
//        assertThat(userEntity.getUserId(), is(userId));
//        assertThat(userEntity.getPassword(), is(password));
//    }
//
//
//}
