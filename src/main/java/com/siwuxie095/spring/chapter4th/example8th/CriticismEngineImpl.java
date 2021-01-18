package com.siwuxie095.spring.chapter4th.example8th;

/**
 * 要注入到 CriticAspect 中的 CriticismEngine 实现
 *
 * @author Jiajing Li
 * @date 2021-01-18 21:51:27
 */
@SuppressWarnings("all")
public class CriticismEngineImpl implements CriticismEngine {

    private String[] criticismPool;

    public void setCriticismPool(String[] criticismPool) {
        this.criticismPool = criticismPool;
    }

    @Override
    public String getCriticism() {
        int i = (int) (Math.random() * criticismPool.length);
        return criticismPool[i];
    }

}
