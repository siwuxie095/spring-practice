package com.siwuxie095.spring.chapter11th.example4th.db;

import com.siwuxie095.spring.chapter11th.example4th.domain.Spittle;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-02-26 08:18:25
 */
@SuppressWarnings("all")
public interface SpittleRepositoryCustom {

    List<Spittle> findRecent();

    List<Spittle> findRecent(int count);

}