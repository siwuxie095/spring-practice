package com.siwuxie095.spring.chapter13th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-03-06 23:10:43
 */
public class Main {

    /**
     * 小结
     *
     * 如果想让应用程序避免一遍遍地为同一个问题推导、计算或查询答案的话，缓存是一种很棒的方式。当以一组参数第一次调用
     * 某个方法时，返回值会被保存在缓存中，如果这个方法再次以相同的参数进行调用时，这个返回值会从缓存中查询获取。在很
     * 多场景中，从缓存查找值会比其他的方式（比如，执行数据库查询）成本更低。因此，缓存会对应用程序的性能带来正面的影
     * 响。
     *
     * 在这里，看到了如何在 Spring 应用中声明缓存。首先，看到的是如何声明一个或更多的 Spring 缓存管理器。然后，将缓
     * 存用到了 Spittr 应用程序中，这是通过将 @Cacheable、@CachePut 和 @CacheEvict 添加到 SpittleRepository
     * 上实现的。
     *
     * 还看到了如何借助 XML 将缓存规则的配置与应用程序代码分离开来。<cache:cacheable>、<cache:cache-put> 和
     * <cache:cache-evict> 元素的作用与注解是一致的。
     *
     * 在这个过程中，讨论了缓存实际上是一种面向切面的行为。Spring 将缓存实现为一个切面。在使用 XML 声明缓存规则时，
     * 这一点非常明显：必须要将缓存通知绑定到一个切点上。
     */
    public static void main(String[] args) {

    }

}
