package com.siwuxie095.spring.chapter17th.example13th.alert;

import com.siwuxie095.spring.chapter17th.example13th.domain.Spittle;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jiajing Li
 * @date 2021-03-26 08:17:34
 */
@SuppressWarnings("all")
public class AlertServiceImpl implements AlertService {

    private RabbitTemplate rabbit;

    @Autowired
    public AlertServiceImpl(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @Override
    public void sendSpittleAlert(Spittle spittle) {
        rabbit.convertAndSend("spittle.alert.exchange",
                "spittle.alerts",
                spittle);
    }

}

