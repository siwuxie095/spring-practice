package com.siwuxie095.spring.chapter4th.example8th;

/**
 * 使用 AspectJ 实现表演的评论员
 *
 * @author Jiajing Li 
 * @date 2021-01-18 21:48:39
 */
@SuppressWarnings("all")
public aspect CriticAspect {

    public CriticAspect() {}

    private CriticismEngine criticismEngine;

    public void setCriticismEngine(CriticismEngine criticismEngine) {
        this.criticismEngine = criticismEngine;
    }

    pointcut performance() : execution(* com.siwuxie095.spring.chapter4th.example8th.Performance.perform(..));

    after() returning : performance() {
        System.out.println(criticismEngine.getCriticism());
    }

}
