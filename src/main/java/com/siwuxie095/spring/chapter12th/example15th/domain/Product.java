package com.siwuxie095.spring.chapter12th.example15th.domain;

import java.io.Serializable;

/**
 * @author Jiajing Li
 * @date 2021-03-04 22:30:35
 */
@SuppressWarnings("all")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sku;
    private String name;
    private float price;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}

