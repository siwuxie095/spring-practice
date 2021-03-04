package com.siwuxie095.spring.chapter12th.example15th;

/**
 * @author Jiajing Li
 * @date 2021-03-04 22:23:43
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 key 和 value 的序列化器
     *
     * 当某个条目保存到 Redis key-value 存储的时候，key 和 value 都会使用 Redis 的序列化器（serializer）进行序列化。
     * Spring Data Redis 提供了多个这样的序列化器，包括：
     * （1）GenericToStringSerializer：使用 Spring 转换服务进行序列化；
     * （2）JacksonJsonRedisSerializer：使用 Jackson 1，将对象序列化为 JSON；
     * （3）Jackson2JsonRedisSerializer：使用 Jackson 2，将对象序列化为 JSON；
     * （4）JdkSerializationRedisSerializer：使用 Java 序列化；
     * （5）OxmSerializer：使用 Spring O/X 映射的编排器和解排器（marshaler 和 unmarshaler）实现序列化，用于 XML 序列化；
     * （6）StringRedisSerializer：序列化 String 类型的 key 和 value。
     *
     * 这些序列化器都实现了 RedisSerializer 接口，如果其中没有符合需求的序列化器，那么你还可以自行创建。
     *
     * RedisTemplate 会使用 JdkSerializationRedisSerializer，这意味着 key 和 value 都会通过 Java 进行序列化。
     * StringRedisTemplate 默认会使用 StringRedisSerializer，这在预料之中，它实际上就是实现 String 与 byte数组
     * 之间的相互转换。这些默认的设置适用于很多的场景，但有时候你可能会发现使用一个不同的序列化器也是很有用处的。
     *
     * 例如，假设当使用 RedisTemplate 的时候，希望将 Product 类型的 value 序列化为 JSON，而 key 是 String 类型。
     * RedisTemplate 的 setKeySerializer() 和 setValueSerializer() 方法就需要如下所示：
     *
     *     @Bean
     *     public RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory cf) {
     *         RedisTemplate<String, Product> redis = new RedisTemplate<String, Product>();
     *         redis.setConnectionFactory(cf);
     *         redis.setKeySerializer(new StringRedisSerializer());
     *         redis.setValueSerializer(new Jackson2JsonRedisSerializer<>(Product.class));
     *         return redis;
     *     }
     *
     * 在这里，设置 RedisTemplate 在序列化 key 的时候，使用 StringRedisSerializer，并且也设置了在序列化 Product
     * 的时候，使用 Jackson2JsonRedisSerializer。
     */
    public static void main(String[] args) {

    }

}
