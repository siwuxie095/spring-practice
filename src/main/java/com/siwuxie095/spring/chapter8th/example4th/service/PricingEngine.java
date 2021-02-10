package com.siwuxie095.spring.chapter8th.example4th.service;

import com.siwuxie095.spring.chapter8th.example4th.domain.Order;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:53:51
 */
@SuppressWarnings("all")
public interface PricingEngine {

    public float calculateOrderTotal(Order order);

}
