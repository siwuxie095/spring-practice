package com.siwuxie095.spring.chapter4th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-01-16 23:37:05
 */
@SuppressWarnings("all")
public class DefaultEncoreable implements Encoreable {

    @Override
    public void performEncore() {
        System.out.println("ENCORE!");
    }
}
