package com.siwuxie095.spring.chapter12th.example6th.db;

import com.siwuxie095.spring.chapter12th.example6th.domain.Order;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-28 20:49:42
 */
@SuppressWarnings("all")
public interface OrderOperations {

    List<Order> findOrdersByType(String type);

}
