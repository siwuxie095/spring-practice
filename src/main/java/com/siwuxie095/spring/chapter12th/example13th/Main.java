package com.siwuxie095.spring.chapter12th.example13th;

/**
 * @author Jiajing Li
 * @date 2021-03-03 22:01:00
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 连接到 Redis
     *
     * Redis 连接工厂会生成到 Redis 数据库服务器的连接。Spring Data Redis 为四种 Redis 客户端实现提供了
     * 连接工厂：
     * （1）JedisConnectionFactory
     * （2）JredisConnectionFactory
     * （3）LettuceConnectionFactory
     * （4）SrpConnectionFactory
     *
     * 具体选择哪一个取决于你。建议你自行测试并建立基准，进而确定哪一种 Redis 客户端和连接工厂最适合你的需求。
     * 从 Spring Data Redis 的角度来看，这些连接工厂在适用性上都是相同的。
     *
     * 在做出决策之后，就可以将连接工厂配置为 Spring 中的 bean。
     *
     * 例如，如下展示了如何配置 JedisConnectionFactory bean：
     *
     *     @Bean
     *     public RedisConnectionFactory redisCF() {
     *         return new JedisConnectionFactory();
     *     }
     *
     * 通过默认构造器创建的连接工厂会向 localhost 上的 6379 端口创建连接，并且没有密码。如果你的 Redis 服
     * 务器运行在其他的主机或端口上，在创建连接工厂的时候，可以设置这些属性：
     *
     *     @Bean
     *     public RedisConnectionFactory redisCF() {
     *         JedisConnectionFactory cf = new JedisConnectionFactory();
     *         cf.setHostName("redis-server");
     *         cf.setPort(7379);
     *         return cf;
     *     }
     *
     * 类似地，如果你的 Redis 服务器配置为需要客户端认证的话，可以通过调用 setPassword() 方法来设置密码：
     *
     *     @Bean
     *     public RedisConnectionFactory redisCF() {
     *         JedisConnectionFactory cf = new JedisConnectionFactory();
     *         cf.setHostName("redis-server");
     *         cf.setPort(7379);
     *         cf.setPassword("foobared");
     *         return cf;
     *     }
     *
     * 在上面的这些例子中，都假设使用的是 JedisConnectionFactory。如果你选择使用其他连接工厂的话，只需进行
     * 简单地替换就可以了。例如，假设你要使用 LettuceConnectionFactory 的话，可以按照如下的方式进行配置：
     *
     *     @Bean
     *     public RedisConnectionFactory redisCF() {
     *         LettuceConnectionFactory cf = new LettuceConnectionFactory();
     *         cf.setHostName("redis-server");
     *         cf.setPort(7379);
     *         cf.setPassword("foobared");
     *         return cf;
     *     }
     *
     * 所有的 Redis 连接工厂都具有 setHostName()、setPort() 和 setPassword() 方法。这样，它们在配置方
     * 面实际上是相同的。
     */
    public static void main(String[] args) {

    }

}
