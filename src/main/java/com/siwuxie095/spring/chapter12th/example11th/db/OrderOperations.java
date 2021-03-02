package com.siwuxie095.spring.chapter12th.example11th.db;

import com.siwuxie095.spring.chapter12th.example11th.domain.Order;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-02 22:25:40
 */
@SuppressWarnings("all")
public interface OrderOperations {

    List<Order> findSpringInActionOrders();

}
