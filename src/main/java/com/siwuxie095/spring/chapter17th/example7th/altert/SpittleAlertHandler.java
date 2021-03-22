package com.siwuxie095.spring.chapter17th.example7th.altert;

import com.siwuxie095.spring.chapter17th.example7th.domain.Spittle;

/**
 * @author Jiajing Li
 * @date 2021-03-22 21:49:20
 */
@SuppressWarnings("all")
public class SpittleAlertHandler {

    public void handleSpittleAlert(Spittle spittle) {
        System.out.println(spittle.getMessage());
    }

}
