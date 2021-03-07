package com.siwuxie095.spring.chapter14th.example3rd.service;

import com.siwuxie095.spring.chapter14th.example3rd.domain.Spittle;
import org.springframework.security.access.annotation.Secured;

/**
 * @author Jiajing Li
 * @date 2021-03-07 16:31:21
 */
@SuppressWarnings("all")
public class SecuredSpittleService implements SpittleService {

    @Override
    @Secured({"ROLE_SPITTER", "ROLE_ADMIN"})
    public void addSpittle(Spittle spittle) {
        System.out.println("Method was called successfully");
    }

}
