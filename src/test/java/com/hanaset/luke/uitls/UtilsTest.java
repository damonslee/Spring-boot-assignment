package com.hanaset.luke.uitls;

import com.hanaset.luke.utils.SecurityUtil;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class UtilsTest {

    @Test
    public void SecurityUtil_암호화_테스트() {

        final String password = "test!!";

        final String result1 = SecurityUtil.sha256(password);
        final String reulst2 = SecurityUtil.sha256(password);

        assertThat(result1, is(reulst2));
    }
}
