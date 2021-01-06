package com.siwuxie095.spring.chapter3rd.example6th;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Jiajing Li
 * @date 2021-01-06 22:46:25
 */
@SuppressWarnings("all")
@Component
public class Table {

    private Dessert dessert;

    @Autowired
    @Qualifier("iceCream")
    public void setDessert(Dessert dessert) {
        this.dessert = dessert;
    }

}
