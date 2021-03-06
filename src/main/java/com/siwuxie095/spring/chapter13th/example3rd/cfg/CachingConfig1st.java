package com.siwuxie095.spring.chapter13th.example3rd.cfg;

import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Jiajing Li
 * @date 2021-03-06 17:21:26
 */
@SuppressWarnings("all")
@Configuration
@EnableCaching
public class CachingConfig1st {

    @Bean
    public EhCacheCacheManager cacheManager(CacheManager cm) {
        return new EhCacheCacheManager(cm);
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean =
                new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(
                new ClassPathResource("com/habuma/spittr/cache/ehchache.xml"));
        return ehCacheManagerFactoryBean;
    }

}
