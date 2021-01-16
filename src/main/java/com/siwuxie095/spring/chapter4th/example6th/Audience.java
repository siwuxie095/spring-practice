package com.siwuxie095.spring.chapter4th.example6th;

import org.aspectj.lang.annotation.*;

/**
 * 观看演出的切面
 *
 * 通过 @Pointcut 注解声明频繁使用的切点表达式
 *
 * @author Jiajing Li
 * @date 2021-01-16 14:58:05
 */
@SuppressWarnings("all")
@Aspect
public class Audience {

    // 定义命名的切点
    @Pointcut("execution(* com.siwuxie095.spring.chapter4th.example6th.Performance.perform(..))")
    public void performance() {}

    // 表演之前
    @Before("performance()")
    public void silenceCellPhones() {
        System.out.println("Silencing cell phones");
    }

    // 表演之前
    @Before("performance()")
    public void takeSeats() {
        System.out.println("Taking seats");
    }

    // 表演之后
    @AfterReturning("performance()")
    public void applause() {
        System.out.println("CLAP CLAP CLAP!!!");
    }

    // 表演失败之后
    @AfterThrowing("performance()")
    public void demandRefund() {
        System.out.println("Demanding a refund");
    }

}
