package com.siwuxie095.spring.chapter17th.example13th.alert;

import com.siwuxie095.spring.chapter17th.example13th.domain.Spittle;

/**
 * @author Jiajing Li
 * @date 2021-03-26 08:16:58
 */
@SuppressWarnings("all")
public interface AlertService {

    void sendSpittleAlert(Spittle spittle);

}
