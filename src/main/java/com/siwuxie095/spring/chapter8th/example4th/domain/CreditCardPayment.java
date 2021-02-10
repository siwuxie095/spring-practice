package com.siwuxie095.spring.chapter8th.example4th.domain;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:25:01
 */
@SuppressWarnings("all")
public class CreditCardPayment extends Payment {

    private String authorization;

    public CreditCardPayment() {
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    @Override
    public String toString() {
        return "CREDIT:  $" + getAmount() + " ; AUTH: " + authorization;
    }

}
