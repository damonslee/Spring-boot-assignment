package com.hanaset.luke.entity;

import com.hanaset.luke.utils.SecurityUtil;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;

public class EntityTest {

    @Test
    public void BankEntity_빌드테스트() {

        final String name = "국민은행";
        final String code = "bnk1";
        final ZonedDateTime regDtime = ZonedDateTime.now();
        final ZonedDateTime updDtime = ZonedDateTime.now();

        final BankEntity bankEntity = BankEntity.builder()
                .instituteName(name)
                .instituteCode(code)
                .regDtime(regDtime)
                .updDtime(updDtime)
                .build();

        assertThat(bankEntity.getInstituteName(), is(name));
        assertThat(bankEntity.getInstituteCode(), is(code));
        assertThat(bankEntity.getRegDtime(), is(regDtime));
        assertThat(bankEntity.getUpdDtime(), is(updDtime));
    }

    @Test
    public void BankHistoryEntity_빌드테스() {

        final Long id = 1L;
        final Long year = 2012L;
        final Long month = 1L;
        final String bankCode = "bnk1";
        final Long price = 2000L;
        final ZonedDateTime regDtime = ZonedDateTime.now();
        final ZonedDateTime updDtime = ZonedDateTime.now();

        final BankHistoryEntity bankHistoryEntity = BankHistoryEntity.builder()
                .id(id)
                .year(year)
                .month(month)
                .bankCode(bankCode)
                .price(price)
                .regDtime(regDtime)
                .updDtime(updDtime)
                .build();

        assertThat(bankHistoryEntity.getId(), is(id));
        assertThat(bankHistoryEntity.getYear(), is(year));
        assertThat(bankHistoryEntity.getMonth(), is(month));
        assertThat(bankHistoryEntity.getBankCode(), is(bankCode));
        assertThat(bankHistoryEntity.getPrice(), is(price));
        assertThat(bankHistoryEntity.getRegDtime(), is(regDtime));
        assertThat(bankHistoryEntity.getUpdDtime(), is(updDtime));
    }

    @Test
    public void UserEntity_빌드테스트() {

        final String userId = "test";
        final String password = SecurityUtil.sha256("test1234!!");
        final ZonedDateTime lastLogin = ZonedDateTime.now();
        final ZonedDateTime regDtime = ZonedDateTime.now();
        final ZonedDateTime updDtime= ZonedDateTime.now();

        final UserEntity userEntity = UserEntity.builder()
                .userId(userId)
                .password(password)
                .lastLogin(lastLogin)
                .regDtime(regDtime)
                .updDtime(updDtime)
                .build();

        assertThat(userEntity.getUserId(), is(userId));
        assertThat(userEntity.getPassword(), is(password));
        assertThat(userEntity.getLastLogin(), is(lastLogin));
        assertThat(userEntity.getRegDtime(), is(regDtime));
        assertThat(userEntity.getUpdDtime(), is(updDtime));
    }
}
