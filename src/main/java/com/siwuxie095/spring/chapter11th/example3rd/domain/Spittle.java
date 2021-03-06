package com.siwuxie095.spring.chapter11th.example3rd.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Jiajing Li
 * @date 2021-02-25 21:42:53
 */
@SuppressWarnings("all")
@Entity
public class Spittle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "spitter")
    private Spitter spitter;
    @Column
    private String message;
    @Column
    private Date postedTime;

    private Spittle() {
    }

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
