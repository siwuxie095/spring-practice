package com.siwuxie095.spring.chapter8th.example4th.domain;

import java.io.Serializable;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:35:00
 */
@SuppressWarnings("all")
public class PaymentDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private PaymentType paymentType;
    private String creditCardNumber;

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

}

