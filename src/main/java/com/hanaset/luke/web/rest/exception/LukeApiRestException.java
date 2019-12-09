package com.hanaset.luke.web.rest.exception;

public class LukeApiRestException extends RuntimeException {

    private String code;

    public LukeApiRestException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
