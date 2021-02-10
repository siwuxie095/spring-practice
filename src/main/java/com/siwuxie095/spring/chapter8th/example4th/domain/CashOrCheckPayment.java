package com.siwuxie095.spring.chapter8th.example4th.domain;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:24:25
 */
@SuppressWarnings("all")
public class CashOrCheckPayment extends Payment {

    public CashOrCheckPayment() {
    }

    @Override
    public String toString() {
        return "CASH or CHECK:  $" + getAmount();
    }

}

