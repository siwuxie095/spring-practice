package com.siwuxie095.spring.chapter12th.example9th.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * @author Jiajing Li
 * @date 2021-03-01 22:24:25
 */
@SuppressWarnings("all")
@RelationshipEntity(type = "HAS_LINE_ITEM_FOR")
public class LineItem {

    @GraphId
    private Long id;

    @StartNode
    private Order order;

    @EndNode
    private Product product;

    private int quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
