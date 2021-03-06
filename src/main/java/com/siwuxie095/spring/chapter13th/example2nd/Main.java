package com.siwuxie095.spring.chapter13th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-03-06 16:07:46
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 启用对缓存的支持
     *
     * Spring 对缓存的支持有两种方式：
     * （1）注解驱动的缓存；
     * （2）XML 声明的缓存；
     *
     * 使用 Spring 的缓存抽象时，最为通用的方式就是在方法上添加 @Cacheable 和 @CacheEvict 注解。在这里，
     * 大多数内容都会使用这种类型的声明式注解。后续还会看到如何使用 XML 来声明缓存边界。
     *
     * 在往 bean 上添加缓存注解之前，必须要启用 Spring 对注解驱动缓存的支持。如果使用 Java 配置的话，那么
     * 可以在其中的一个配置类上添加 @EnableCaching，这样的话就能启用注解驱动的缓存。如下代码展现了如何实际
     * 使用 @EnableCaching。
     *
     * @Configuration
     * @EnableCaching
     * public class CachingConfig {
     *
     *     @Bean
     *     public CacheManager cacheManager() {
     *         return new ConcurrentMapCacheManager();
     *     }
     *
     * }
     *
     * 如果以 XML 的方式配置应用的话，那么可以使用 Spring cache 命名空间中的 <cache:annotation-driven>
     * 元素来启用注解驱动的缓存。
     *
     * <!-- 启用缓存 -->
     * <cache:annotation-driven />
     *
     * <!-- 声明缓存管理器 -->
     * <bean id="cacheManager"
     * class="org.springframework.cache.concurrent.ConcurrentMapCacheManager#ConcurrentMapCacheManager()" />
     *
     * PS：需要引入 cache 命名空间。
     *
     * 其实在本质上，@EnableCaching 和 <cache:annotation-driven> 的工作方式是相同的。它们都会创建一个
     * 切面（aspect）并触发 Spring 缓存注解的切点（pointcut）。根据所使用的注解以及缓存的状态，这个切面会
     * 从缓存中获取数据，将数据添加到缓存之中或者从缓存中移除某个值。
     *
     * 在这两种配置方式中，不仅仅启用了注解驱动的缓存，还声明了一个缓存管理器（cache manager）的 bean。缓
     * 存管理器是 Spring 缓存抽象的核心，它能够与多个流行的缓存实现进行集成。
     *
     * 在本例中，声明了 ConcurrentMapCacheManager，这个简单的缓存管理器使用 ConcurrentHashMap 作为其
     * 缓存存储。它非常简单，因此对于开发、测试或基础的应用来讲，这是一个很不错的选择。但它的缓存存储是基于
     * 内存的，所以它的生命周期是与应用关联的，对于生产级别的大型企业级应用程序，这可能并不是理想的选择。
     *
     * 幸好，有多个很棒的缓存管理器方案可供使用。后续将会介绍一下几个最为常用的缓存管理器。
     */
    public static void main(String[] args) {

    }

}
