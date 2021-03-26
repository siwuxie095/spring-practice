package com.siwuxie095.spring.chapter17th.example13th.alert;

import com.siwuxie095.spring.chapter17th.example13th.domain.Spittle;

/**
 * @author Jiajing Li
 * @date 2021-03-26 08:18:09
 */
@SuppressWarnings("all")
public class SpittleAlertHandler {

    public void handleSpittleAlert(Spittle spittle) {
        System.out.println(spittle.getMessage());
    }

}
