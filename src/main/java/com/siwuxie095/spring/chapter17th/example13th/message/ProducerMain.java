package com.siwuxie095.spring.chapter17th.example13th.message;

import com.siwuxie095.spring.chapter17th.example13th.domain.Spittle;
import org.junit.Ignore;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * @author Jiajing Li
 * @date 2021-03-26 08:19:19
 */
@SuppressWarnings("all")
@Ignore
public class ProducerMain {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/amqp-producer.xml");
        AmqpTemplate template = (AmqpTemplate) context.getBean("rabbitTemplate");

        for (int i = 0; i < 20; i++) {
            System.out.println("Sending message #" + i);
            Spittle spittle = new Spittle((long) i, null, "Hello world (" + i + ")", new Date());
            template.convertAndSend(spittle);
            Thread.sleep(5000);
        }

        System.out.println("Done!");
    }

}

