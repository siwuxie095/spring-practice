package com.siwuxie095.spring.chapter4th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-01-17 22:17:52
 */
@SuppressWarnings("all")
public class DefaultEncoreable implements Encoreable {

    @Override
    public void performEncore() {
        System.out.println("ENCORE!");
    }
}
