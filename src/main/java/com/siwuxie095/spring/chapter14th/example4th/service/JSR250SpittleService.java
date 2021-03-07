package com.siwuxie095.spring.chapter14th.example4th.service;

import com.siwuxie095.spring.chapter14th.example4th.domain.Spittle;

import javax.annotation.security.RolesAllowed;

/**
 * @author Jiajing Li
 * @date 2021-03-07 16:49:31
 */
@SuppressWarnings("all")
public class JSR250SpittleService implements SpittleService {

    @Override
    @RolesAllowed("ROLE_SPITTER")
    public void addSpittle(Spittle spittle) {
        System.out.println("Method was called successfully");
    }

}
