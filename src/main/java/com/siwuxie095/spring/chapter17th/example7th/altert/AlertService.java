package com.siwuxie095.spring.chapter17th.example7th.altert;

import com.siwuxie095.spring.chapter17th.example7th.domain.Spittle;

/**
 * @author Jiajing Li
 * @date 2021-03-22 21:48:05
 */
@SuppressWarnings("all")
public interface AlertService {

    void sendSpittleAlert(Spittle spittle);

    Spittle retrieveSpittleAlert();

}
