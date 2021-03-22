package com.siwuxie095.spring.chapter17th.example7th.altert;

import com.siwuxie095.spring.chapter17th.example7th.domain.Spittle;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * @author Jiajing Li
 * @date 2021-03-22 21:50:00
 */
public class SpittleJmsMain {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/messaging.xml");
        AlertService alertService = context.getBean(AlertService.class);

        Spittle spittle = new Spittle(1L, null, "Hello", new Date());
        alertService.sendSpittleAlert(spittle);
    }

}

