package com.siwuxie095.spring.chapter14th.example6th.service;

import com.siwuxie095.spring.chapter14th.example6th.domain.Spittle;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * @author Jiajing Li
 * @date 2021-03-07 17:17:31
 */
@SuppressWarnings("all")
public class ExpressionSecuredSpittleService implements SpittleService {

    @Override
    @PreAuthorize("(hasRole('ROLE_SPITTER') and #spittle.text.length() le 140) " +
            "or hasRole('ROLE_PREMIUM')")
    public void addSpittle(Spittle spittle) {
        System.out.println("Method was called successfully");
    }

}

