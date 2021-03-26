package com.siwuxie095.spring.chapter17th.example13th.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Jiajing Li
 * @date 2021-03-26 08:15:45
 */
@SuppressWarnings("all")
public class Spittle implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Spitter spitter;
    private String message;
    private Date postedTime;

    public Spittle(Long id, Spitter spitter, String message, Date postedTime) {
        this.id = id;
        this.spitter = spitter;
        this.message = message;
        this.postedTime = postedTime;
    }

    public Long getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public Date getPostedTime() {
        return this.postedTime;
    }

    public Spitter getSpitter() {
        return this.spitter;
    }

}
