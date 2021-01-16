package com.siwuxie095.spring.chapter4th.example6th;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 使用环绕通知重新实现 Audience 切面
 *
 * @author Jiajing Li
 * @date 2021-01-16 15:46:29
 */
@SuppressWarnings("all")
@Aspect
public class AroundAudience {

    // 定义命名的切点
    @Pointcut("execution(* com.siwuxie095.spring.chapter4th.example6th.Performance.perform(..))")
    public void performance() {}

    // 环绕通知方法
    @Around("performance()")
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
