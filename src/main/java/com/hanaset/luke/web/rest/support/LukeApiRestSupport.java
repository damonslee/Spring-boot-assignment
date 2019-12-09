package com.hanaset.luke.web.rest.support;

import com.google.common.collect.ImmutableMap;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class LukeApiRestSupport {

    protected static <T> ResponseEntity<?> success(T data) {
        return ResponseEntity.ok(
                ImmutableMap.of(
                        "code", "0",
                        "data", data
                )
        );
    }

    protected static <T> ResponseEntity<?> response(T data, String key, String value) {
        return ResponseEntity.ok(
                ImmutableMap.of(
                        key, value,
                        "data", data != null ? data : new JSONObject()
                )
        );
    }

    protected static <T> ResponseEntity<?> lukeResponseException(String code, String msg) {
        return new ResponseEntity<>(
                ImmutableMap.of(
                        "code", code,
                        "msg", msg,
                        "data", "{}"
                ), HttpStatus.BAD_REQUEST
        );
    }
}
