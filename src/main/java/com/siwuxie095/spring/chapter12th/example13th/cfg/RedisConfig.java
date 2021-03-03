package com.siwuxie095.spring.chapter12th.example13th.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author Jiajing Li
 * @date 2021-03-03 22:10:22
 */
@SuppressWarnings("all")
@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisCF1st() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisConnectionFactory redisCF2nd() {
        JedisConnectionFactory cf = new JedisConnectionFactory();
        cf.setHostName("redis-server");
        cf.setPort(7379);
        return cf;
    }

    @Bean
    public RedisConnectionFactory redisCF3rd() {
        JedisConnectionFactory cf = new JedisConnectionFactory();
        cf.setHostName("redis-server");
        cf.setPort(7379);
        cf.setPassword("foobared");
        return cf;
    }

    @Bean
    public RedisConnectionFactory redisCF4th() {
        LettuceConnectionFactory cf = new LettuceConnectionFactory();
        cf.setHostName("redis-server");
        cf.setPort(7379);
        cf.setPassword("foobared");
        return cf;
    }

}
