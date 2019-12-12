package com.hanaset.luke.utils;

import com.hanaset.luke.web.rest.exception.ErrorCode;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SecurityUtil {

    public static String sha256(String msg) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(msg.getBytes());
            return byteToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new LukeApiRestException(ErrorCode.SIGN_UP_FAILED, "비밀번호 암화화 중 오류입니다. 잠시후 다시 시도해주세요.");
        }
    }

    public static String byteToHexString(byte[] data) {

        StringBuilder sb = new StringBuilder();

        for (byte b : data) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


}
