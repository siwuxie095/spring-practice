package com.siwuxie095.spring.chapter8th.example4th.domain;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:27:53
 */
@SuppressWarnings("all")
public enum PaymentType {

    CASH, CHECK, CREDIT_CARD;

    public static List<PaymentType> asList() {
        PaymentType[] all = PaymentType.values();
        return Arrays.asList(all);
    }

    @Override
    public String toString() {
        return WordUtils.capitalizeFully(name().replace('_', ' '));
    }

}

