package com.siwuxie095.spring.chapter8th.example4th.service;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:53:18
 */
@SuppressWarnings("all")
public class PaymentProcessor {

    public PaymentProcessor() {
    }

    public void approveCreditCard(String creditCardNumber, String expMonth,
                                  String expYear, float amount) throws PaymentException {
        if (amount > 20.00) {
            throw new PaymentException();
        }
    }
}

