package com.siwuxie095.spring.chapter15th.example6th.domain;

import java.util.Date;

/**
 * @author Jiajing Li
 * @date 2021-03-14 21:45:09
 */
@SuppressWarnings("all")
public class Spittle {

    private final Long id;
    private final String text;
    private final Date postedTime;
    private Spitter spitter;

    public Spittle(Long id, Spitter spitter, String text, Date postedTime) {
        this.id = id;
        this.spitter = spitter;
        this.text = text;
        this.postedTime = postedTime;
    }

    public Long getId() {
        return this.id;
    }

    public String getText() {
        return this.text;
    }

    public Date getPostedTime() {
        return this.postedTime;
    }

    public Spitter getSpitter() {
        return spitter;
    }

}
