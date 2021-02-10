package com.siwuxie095.spring.chapter8th.example4th.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:32:02
 */
@SuppressWarnings("all")
public class Pizza implements Serializable {

    private PizzaSize size;
    private List<Topping> toppings;

    public Pizza() {
        toppings = new ArrayList<Topping>();
        size = PizzaSize.LARGE;
    }

    public PizzaSize getSize() {
        return size;
    }

    public void setSize(PizzaSize size) {
        this.size = size;
    }

    public void setSize(String sizeString) {
        this.size = PizzaSize.valueOf(sizeString);
    }

    public List<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(List<Topping> toppings) {
        this.toppings = toppings;
    }

    public void setToppings(String[] toppingStrings) {
        for (int i = 0; i < toppingStrings.length; i++) {
            toppings.add(Topping.valueOf(toppingStrings[i]));
        }
    }

}

