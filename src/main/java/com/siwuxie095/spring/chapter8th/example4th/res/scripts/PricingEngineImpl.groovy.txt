package com.siwuxie095.spring.chapter8th.example4th.res.scripts

import com.siwuxie095.spring.chapter8th.example4th.domain.Order
import com.siwuxie095.spring.chapter8th.example4th.service.PricingEngine


class PricingEngineImpl implements PricingEngine, Serializable {

    public float calculateOrderTotal(Order order) {
        print "IN GROOVY";

        retun 99.99;
    }

}
