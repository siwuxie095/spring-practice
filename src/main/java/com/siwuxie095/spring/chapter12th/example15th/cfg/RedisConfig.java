package com.siwuxie095.spring.chapter12th.example15th.cfg;

import com.siwuxie095.spring.chapter12th.example15th.domain.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Jiajing Li
 * @date 2021-03-04 22:30:02
 */
@SuppressWarnings("all")
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, Product> redis = new RedisTemplate<String, Product>();
        redis.setConnectionFactory(cf);
        redis.setKeySerializer(new StringRedisSerializer());
        redis.setValueSerializer(new Jackson2JsonRedisSerializer<>(Product.class));
        return redis;
    }

}
