package com.siwuxie095.spring.chapter8th.example4th.domain;

import org.apache.commons.lang3.text.WordUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:31:16
 */
@SuppressWarnings("all")
public enum Topping implements Serializable {

    PEPPERONI,
    SAUSAGE,
    HAMBURGER,
    MUSHROOM,
    CANADIAN_BACON,
    PINEAPPLE,
    GREEN_PEPPER,
    JALAPENO,
    TOMATO,
    ONION,
    EXTRA_CHEESE;

    public static List<Topping> asList() {
        Topping[] all = Topping.values();
        return Arrays.asList(all);
    }

    @Override
    public String toString() {
        return WordUtils.capitalizeFully(name().replace('_', ' '));
    }

}

