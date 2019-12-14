package com.hanaset.luke.web.rest.advice;

import com.hanaset.luke.web.rest.exception.ErrorCode;
import com.hanaset.luke.web.rest.exception.LukeApiRestException;
import com.hanaset.luke.web.rest.support.LukeApiRestSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class LukeApiRestAdvice extends LukeApiRestSupport {

    @ExceptionHandler(LukeApiRestException.class)
    public ResponseEntity handleLukeResponseException(LukeApiRestException e) {
        log.error("[Exception Issue] ===> {} / {}", e.getCode(), e.getMessage());
        return lukeResponseException(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError msg = e.getBindingResult().getFieldError();
        return lukeResponseException(ErrorCode.REQUEST_ERROR, msg.getDefaultMessage());
    }
}
