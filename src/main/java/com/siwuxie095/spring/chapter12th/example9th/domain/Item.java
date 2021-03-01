package com.siwuxie095.spring.chapter12th.example9th.domain;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * @author Jiajing Li
 * @date 2021-03-01 22:13:43
 */
@SuppressWarnings("all")
@NodeEntity
public class Item {

    @GraphId
    private Long id;

    private Order order;

    private String product;

    private double price;

    private int quantity;

    public Order getOrder() {
        return order;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

}
