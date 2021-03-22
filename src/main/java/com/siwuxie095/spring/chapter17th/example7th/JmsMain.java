package com.siwuxie095.spring.chapter17th.example7th;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsOperations;

/**
 * @author Jiajing Li
 * @date 2021-03-22 21:50:56
 */
public class JmsMain {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/messaging.xml");
        JmsOperations jms = context.getBean(JmsOperations.class);
        for (int i = 0; i < 10; i++) {
            jms.convertAndSend("hello.queue", "Hello");
        }
        context.close();
    }

}

