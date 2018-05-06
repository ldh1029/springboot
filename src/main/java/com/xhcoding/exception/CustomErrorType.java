package com.xhcoding.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Created by xin on 2017/11/28.
 */
@Data
public class CustomErrorType {
    private Integer statusCode;
    private String msg;

    public CustomErrorType(Integer statusCode, String msg, HttpStatus status) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
