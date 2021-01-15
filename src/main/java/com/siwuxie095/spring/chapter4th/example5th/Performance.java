package com.siwuxie095.spring.chapter4th.example5th;

import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Jiajing Li
 * @date 2021-01-15 20:16:35
 */
@SuppressWarnings("all")
public interface Performance {

    @Pointcut()
    void perform();

}
