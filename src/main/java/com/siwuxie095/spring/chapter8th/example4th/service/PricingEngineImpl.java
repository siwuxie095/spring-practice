package com.siwuxie095.spring.chapter8th.example4th.service;

import com.siwuxie095.spring.chapter8th.example4th.domain.Order;
import com.siwuxie095.spring.chapter8th.example4th.domain.Pizza;
import com.siwuxie095.spring.chapter8th.example4th.domain.PizzaSize;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:54:26
 */
@SuppressWarnings("all")
public class PricingEngineImpl implements PricingEngine, Serializable {

    private static final Logger LOGGER =
            Logger.getLogger(PricingEngineImpl.class);

    private static Map<PizzaSize, Float> SIZE_PRICES;
    private static float PRICE_PER_TOPPING = 0.20f;

    static {
        SIZE_PRICES = new HashMap<PizzaSize, Float>();
        SIZE_PRICES.put(PizzaSize.SMALL, 6.99f);
        SIZE_PRICES.put(PizzaSize.MEDIUM, 7.99f);
        SIZE_PRICES.put(PizzaSize.LARGE, 8.99f);
        SIZE_PRICES.put(PizzaSize.GINORMOUS, 9.99f);
    }

    public PricingEngineImpl() {
    }

    @Override
    public float calculateOrderTotal(Order order) {
        LOGGER.debug("Calculating order total");

        float total = 0.0f;

        for (Pizza pizza : order.getPizzas()) {
            float pizzaPrice = SIZE_PRICES.get(pizza.getSize());
            if (pizza.getToppings().size() > 2) {
                pizzaPrice += (pizza.getToppings().size() *
                        PRICE_PER_TOPPING);
            }
            total += pizzaPrice;
        }

        return total;
    }

}
