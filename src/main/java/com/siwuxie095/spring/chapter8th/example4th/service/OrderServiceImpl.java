package com.siwuxie095.spring.chapter8th.example4th.service;

import com.siwuxie095.spring.chapter8th.example4th.domain.Order;
import org.apache.log4j.Logger;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:38:48
 */
@SuppressWarnings("all")
public class OrderServiceImpl {

    private static final Logger LOGGER =
            Logger.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl() {
    }

    public void saveOrder(Order order) {
        LOGGER.debug("SAVING ORDER:  ");
        LOGGER.debug("   Customer:  " + order.getCustomer().getName());
        LOGGER.debug("   # of Pizzas:  " + order.getPizzas().size());
        LOGGER.debug("   Payment:  " + order.getPayment());
    }
}

