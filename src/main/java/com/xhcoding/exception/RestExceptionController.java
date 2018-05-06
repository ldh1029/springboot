package com.xhcoding.exception;

import com.google.common.base.Preconditions;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xin on 2017/11/28.
 */
@ControllerAdvice
public class RestExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity handleExceptionController(HttpServletRequest request, Throwable throwable) {
        HttpStatus status = getStatus(request);
        return ResponseEntity.badRequest().body(new CustomErrorType(status.value(),throwable.getMessage(),status));
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Preconditions.checkNotNull(statusCode,HttpStatus.INTERNAL_SERVER_ERROR);
        return HttpStatus.valueOf(statusCode);
    }
}
