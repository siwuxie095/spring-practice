package com.siwuxie095.spring.chapter18th.example8th.spittr;

/**
 * @author Jiajing Li
 * @date 2021-03-28 17:06:23
 */
@SuppressWarnings("all")
public class Notification {

    private String message;

    public Notification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

