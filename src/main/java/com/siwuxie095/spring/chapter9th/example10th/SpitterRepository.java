package com.siwuxie095.spring.chapter9th.example10th;

/**
 * @author Jiajing Li
 * @date 2021-02-17 17:12:07
 */
@SuppressWarnings("all")
public interface SpitterRepository {

    Spitter save(Spitter spitter);

    Spitter findByUsername(String username);

}
