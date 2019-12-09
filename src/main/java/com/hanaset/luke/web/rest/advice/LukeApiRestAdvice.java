package com.hanaset.luke.web.rest.advice;

import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import com.hanaset.luke.web.rest.support.LukeApiRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LukeApiRestAdvice extends LukeApiRestSupport {

    @ExceptionHandler(LukeApiRestException.class)
    public ResponseEntity handleLukeResponseException(LukeApiRestException e) {
        return lukeResponseException(e.getCode(), e.getMessage());
    }
}
