package com.siwuxie095.spring.chapter4th.example7th;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * watchPerformance() 方法提供了 AOP 环绕通知
 *
 * @author Jiajing Li
 * @date 2021-01-17 18:01:21
 */
@SuppressWarnings("all")
public class AroundAudience {

    public void watchPerformance(ProceedingJoinPoint pjp) {
        try {
            System.out.println("Silencing cell phones");
            System.out.println("Taking seats");
            pjp.proceed();
            System.out.println("CLAP CLAP CLAP!!!");
        } catch (Throwable e) {
            System.out.println("Demanding a refund");
        }
    }

}
