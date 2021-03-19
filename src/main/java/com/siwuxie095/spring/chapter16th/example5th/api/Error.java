package com.siwuxie095.spring.chapter16th.example5th.api;

/**
 * @author Jiajing Li
 * @date 2021-03-18 22:49:09
 */
@SuppressWarnings("all")
public class Error {

    private int code;
    private String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

