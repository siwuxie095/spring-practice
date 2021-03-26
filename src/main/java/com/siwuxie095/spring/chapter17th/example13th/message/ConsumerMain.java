package com.siwuxie095.spring.chapter17th.example13th.message;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jiajing Li
 * @date 2021-03-26 08:18:47
 */
@SuppressWarnings("all")
public class ConsumerMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/amqp-consumer.xml");
    }

}
