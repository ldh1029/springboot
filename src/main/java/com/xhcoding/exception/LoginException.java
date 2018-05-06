package com.xhcoding.exception;

/**
 * Created by xin on 2018/1/24.
 */
public class LoginException extends Exception {
    String msg;

    public LoginException(String msg) {
        this.msg = msg;
    }
}
