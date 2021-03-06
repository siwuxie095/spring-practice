package com.siwuxie095.spring.chapter13th.example3rd.cfg;

import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2021-03-06 17:55:23
 */
@SuppressWarnings("all")
@Configuration
public class CachingConfig3rd {

    @Bean
    public CacheManager cacheManager(
            net.sf.ehcache.CacheManager cm,
            javax.cache.CacheManager jcm,
            RedisTemplate redisTemplate) {
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        List<CacheManager> managers = new ArrayList<>();
        managers.add(new JCacheCacheManager(jcm));
        managers.add(new EhCacheCacheManager(cm));
        managers.add(new RedisCacheManager(redisTemplate));
        cacheManager.setCacheManagers(managers);
        return cacheManager;
    }

}
