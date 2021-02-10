package com.siwuxie095.spring.chapter8th.example4th.domain;

import java.io.Serializable;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:23:26
 */
@SuppressWarnings("all")
public abstract class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    private float amount;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}

