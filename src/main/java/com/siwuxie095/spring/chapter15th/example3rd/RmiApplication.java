package com.siwuxie095.spring.chapter15th.example3rd;

import com.siwuxie095.spring.chapter15th.example3rd.cfg.RmiConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Jiajing Li
 * @date 2021-03-09 22:39:03
 */
@SuppressWarnings("all")
public class RmiApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(RmiConfig.class);
        System.out.println("Rmi Service is running.");
    }

}
