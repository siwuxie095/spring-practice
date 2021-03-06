package com.siwuxie095.spring.chapter13th.example4th.db;

import com.siwuxie095.spring.chapter13th.example4th.domain.Spittle;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-06 20:25:26
 */
@SuppressWarnings("all")
public interface SpittleRepository {

    long count();

    @Cacheable("spittleCache")
    List<Spittle> findRecent();

    List<Spittle> findRecent(int count);

    @Cacheable("spittleCache")
    Spittle findOne(long id);

    @CachePut(value = "spittleCache", key = "#result.id")
    Spittle save(Spittle spittle);

    @Cacheable("spittleCache")
    List<Spittle> findBySpitterId(long spitterId);

    @CacheEvict(value = "spittleCache", condition = "")
    void delete(long id);

}
