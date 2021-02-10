package com.siwuxie095.spring.chapter8th.example4th.service;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:35:45
 */
@SuppressWarnings("all")
public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

}
