package com.siwuxie095.spring.chapter3rd.example7th;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Jiajing Li
 * @date 2021-01-07 21:31:22
 */
@SuppressWarnings("all")
@Component
public class StoreService {

    private ShoppingCart shoppingCart;

    @Autowired
    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

}
