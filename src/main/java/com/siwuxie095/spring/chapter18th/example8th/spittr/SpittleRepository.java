package com.siwuxie095.spring.chapter18th.example8th.spittr;

/**
 * @author Jiajing Li
 * @date 2021-03-28 17:03:52
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    Spittle save(Spittle spittle);

    Spittle findOne(Long id);

}
