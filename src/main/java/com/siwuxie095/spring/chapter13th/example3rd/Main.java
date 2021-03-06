package com.siwuxie095.spring.chapter13th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-03-06 16:59:14
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 配置缓存管理器
     *
     * Spring 3.1 内置了五个缓存管理器实现，如下所示：
     * （1）SimpleCacheManager
     * （2）NoOpCacheManager
     * （3）ConcurrentMapCacheManager
     * （4）CompositeCacheManager
     * （5）EhCacheCacheManager
     *
     * Spring 3.2 引入了另外一个缓存管理器，这个管理器可以用在基于 JCache（JSR-107）的缓存提供商之中。除了核心
     * 的 Spring 框架，Spring Data 又提供了两个缓存管理器：
     * （1）RedisCacheManager（来自于 Spring Data Redis 项目）
     * （2）GemfireCacheManager（来自于 Spring Data GemFire 项目）
     *
     * 所以可以看到，在为 Spring 的缓存抽象选择缓存管理器时，有很多可选方案。具体选择哪一个要取决于想要使用的底层
     * 缓存供应商。每一个方案都可以为应用提供不同风格的缓存，其中有一些会比其他的更加适用于生产环境。尽管所做出的
     * 选择会影响到数据如何缓存，但是 Spring 声明缓存的方式上并没有什么差别。
     *
     * 必须选择一个缓存管理器，然后要在 Spring 应用上下文中，以 bean 的形式对其进行配置。
     *
     * 已经看到了如何配置 ConcurrentMapCacheManager，并且知道它可能并不是实际应用的最佳选择。现在，看一下如何
     * 配置 Spring 其他的缓存管理器，从 EhCacheCacheManager 开始吧。
     *
     *
     *
     * 1、使用 Ehcache 缓存
     *
     * Ehcache 是最为流行的缓存供应商之一。Ehcache 网站上说它是 "Java 领域应用最为广泛的缓存"。鉴于它的广泛采用，
     * Spring 提供集成 Ehcache 的缓存管理器是很有意义的。这个缓存管理器也就是 EhCacheCacheManager。
     *
     * 当读这个名字的时候，在 cache 这个词上似乎有点结结巴巴的感觉。在 Spring 中配置 EhCacheCacheManager 是很
     * 容易的。如下代码展现了如何在 Java 中对其进行配置。
     *
     * @Configuration
     * @EnableCaching
     * public class CachingConfig {
     *
     *     @Bean
     *     public EhCacheCacheManager cacheManager(CacheManager cm) {
     *         return new EhCacheCacheManager(cm);
     *     }
     *
     *     @Bean
     *     public EhCacheManagerFactoryBean ehcache() {
     *         EhCacheManagerFactoryBean ehCacheManagerFactoryBean =
     *                 new EhCacheManagerFactoryBean();
     *         ehCacheManagerFactoryBean.setConfigLocation(
     *                 new ClassPathResource("com/habuma/spittr/cache/ehchache.xml"));
     *         return ehCacheManagerFactoryBean;
     *     }
     *
     * }
     *
     * 其中，cacheManager() 方法创建了一个 EhCacheCacheManager 的实例，这是通过传入 Ehcache CacheManager
     * 实例实现的。在这里，稍微有点诡异的注入可能会让人感觉迷惑，这是因为 Spring 和 EhCache 都定义了 CacheManager
     * 类型。需要明确的是，EhCache 的 CacheManager 要被注入到 Spring 的 EhCacheCacheManager 之中。
     *
     * PS：EhCacheCacheManager 是 Spring CacheManager 的实现。
     *
     * 由于需要使用 EhCache 的 CacheManager 来进行注入，所以必须也要声明一个 CacheManager bean。为了对其进行
     * 简化，Spring 提供了 EhCacheManagerFactoryBean 来生成 EhCache 的 CacheManager。方法 ehcache() 会
     * 创建并返回一个 EhCacheManagerFactoryBean 实例。因为它是一个工厂 bean（也就是说，它实现了 Spring 的
     * FactoryBean接口），所以注册在 Spring 应用上下文中的并不是 EhCacheManagerFactoryBean 的实例，而是
     * CacheManager 的一个实例，因此适合注入到 EhCacheCacheManager 之中。
     *
     * 除了在 Spring 中配置的 bean，还需要有针对 EhCache 的配置。EhCache 为 XML 定义了自己的配置模式，需要在
     * 一个 XML 文件中配置缓存，该文件需要符合 EhCache 所定义的模式。在创建 EhCacheManagerFactoryBean 的过程
     * 中，需要告诉它 EhCache 配置文件在什么地方。
     *
     * 在这里通过调用 setConfigLocation() 方法，传入 ClassPathResource，用来指明 EhCache XML 配置文件相对于
     * 根类路径（classpath）的位置。
     *
     * 至于 ehcache.xml 文件的内容，不同的应用之间会有所差别，但是至少需要声明一个最小的缓存。例如，如下的 EhCache
     * 配置声明一个名为 spittleCache 的缓存，它最大的堆存储为 50 MB，存活时间为 100 秒：
     *
     * <ehcache>
     *     <cache name="spittleCache"
     *            maxBytesLocalHeap="50m"
     *            timeToLiveSeconds="100">
     *     </cache>
     * </ehcache>
     *
     * 显然，这是一个基础的 EhCache 配置。在你的应用之中，可能需要使用 EhCache 所提供的丰富的配置选项。
     *
     * PS：参考 EhCache 的文档以了解调优 EhCache 配置的细节，地址是：
     * https://www.ehcache.org/documentation/configuration
     *
     *
     *
     * 2、使用 Redis 缓存
     *
     * 如果你仔细想一下的话，缓存的条目不过是一个键值对（key-value pair），其中 key 描述了产生 value 的操作和参
     * 数。因此，很自然地就会想到，Redis 作为 key-value 存储，非常适合于存储缓存。
     *
     * Redis 可以用来为 Spring 缓存抽象机制存储缓存条目，Spring Data Redis 提供了 RedisCacheManager，这是
     * CacheManager 的一个实现。RedisCacheManager 会与一个 Redis 服务器协作，并通过 RedisTemplate 将缓存
     * 条目存储到 Redis 中。
     *
     * 为了使用 RedisCacheManager，需要 RedisTemplate bean 以及 RedisConnectionFactory 实现类的一个 bean。
     *
     *
     *
     *
     * 在 RedisTemplate 就绪之后，配置 RedisCacheManager 就是非常简单的事情了，如下所示。
     *
     * @Configuration
     * @EnableCaching
     * public class CachingConfig {
     *
     *     @Bean
     *     public CacheManager cacheManager(RedisTemplate redisTemplate) {
     *         return new RedisCacheManager(redisTemplate);
     *     }
     *
     *     @Bean
     *     public JedisConnectionFactory redisConnectionFactory() {
     *         JedisConnectionFactory jedisConnectionFactory =
     *                 new JedisConnectionFactory();
     *         jedisConnectionFactory.afterPropertiesSet();
     *         return jedisConnectionFactory;
     *     }
     *
     *     @Bean
     *     public RedisTemplate<String, String> redisTemplate(
     *             RedisConnectionFactory redisConnectionFactory) {
     *         RedisTemplate<String, String> redisTemplate =
     *                 new RedisTemplate<>();
     *         redisTemplate.setConnectionFactory(redisConnectionFactory);
     *         redisTemplate.afterPropertiesSet();
     *         return redisTemplate;
     *     }
     *
     * }
     *
     * 可以看到，这里构建了一个 RedisCacheManager，这是通过传递一个 RedisTemplate 实例作为其构造器的参数实现的。
     *
     *
     *
     * 3、使用多个缓存管理器
     *
     * 并不是只能有且仅有一个缓存管理器。如果你很难确定该使用哪个缓存管理器，或者有合法的技术理由使用超过一个缓存管
     * 理器的话，那么可以尝试使用 Spring 的 CompositeCacheManager。
     *
     * CompositeCacheManager 要通过一个或更多的缓存管理器来进行配置，它会迭代这些缓存管理器，以查找之前所缓存的
     * 值。
     *
     * 以下代码展现了如何创建 CompositeCacheManager bean，它会迭代 JCacheCacheManager、EhCacheCacheManager
     * 和 RedisCacheManager。
     *
     *     @Bean
     *     public CacheManager cacheManager(
     *             net.sf.ehcache.CacheManager cm,
     *             javax.cache.CacheManager jcm,
     *             RedisTemplate redisTemplate) {
     *         CompositeCacheManager cacheManager = new CompositeCacheManager();
     *         List<CacheManager> managers = new ArrayList<>();
     *         managers.add(new JCacheCacheManager(jcm));
     *         managers.add(new EhCacheCacheManager(cm));
     *         managers.add(new RedisCacheManager(redisTemplate));
     *         cacheManager.setCacheManagers(managers);
     *         return cacheManager;
     *     }
     *
     * 当查找缓存条目时，CompositeCacheManager 首先会从 JCacheCacheManager 开始检查 JCache 实现，然后通过
     * EhCacheCacheManager 检查 Ehcache，最后会使用 RedisCacheManager 来检查 Redis，完成缓存条目的查找。
     *
     * 在配置完缓存管理器并启用缓存后，就可以在 bean 方法上应用缓存规则了。后续将会介绍如何使用 Spring 的缓存注解
     * 来定义缓存边界。
     */
    public static void main(String[] args) {

    }

}
