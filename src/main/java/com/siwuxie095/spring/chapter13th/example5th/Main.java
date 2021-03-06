package com.siwuxie095.spring.chapter13th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-03-06 22:18:53
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 XML 声明缓存
     *
     * 你可能想要知道为什么想要以XML的方式声明缓存。毕竟，缓存注解要优雅得多。
     *
     * 有两个原因：
     * （1）你可能会觉得在自己的源码中添加 Spring 的注解有点不太舒服；
     * （2）你需要在没有源码的 bean 上应用缓存功能。
     *
     * 在上面的任意一种情况下，最好（或者说需要）将缓存配置与缓存数据的代码分隔开来。Spring 的 cache 命名
     * 空间提供了使用 XML 声明缓存规则的方法，可以作为面向注解缓存的替代方案。因为缓存是一种面向切面的行为，
     * 所以 cache 命名空间会与 Spring 的 aop 命名空间结合起来使用，用来声明缓存所应用的切点在哪里。
     *
     * 要开始配置 XML 声明的缓存，首先需要创建 Spring 配置文件，这个文件中要包含 cache 和 aop 命名空间。
     *
     * cache 命名空间定义了在 Spring XML 配置文件中声明缓存的配置元素。如下列出了 cache 命名空间所提供的
     * 所有元素。
     * （1）<cache:annotation-driven>：
     * 启用注解驱动的缓存。等同于 Java 配置中的 @EnableCaching；
     * （2）<cache:advice>：
     * 定义缓存通知（advice）。结合 <aop:advisor>，将通知应用到切点上；
     * （3）<cache:caching>：
     * 在缓存通知中，定义一组特定的缓存规则；
     * （4）<cache:cacheable>：
     * 指明某个方法要进行缓存。等同于 @Cacheable 注解；
     * （5）<cache:cache-put>：
     * 指明某个方法要填充缓存，但不会考虑缓存中是否已有匹配的值。等同于 @CachePut 注解；
     * （6）<cache:cache-evict>：
     * 指明某个方法要从缓存中移除一个或多个条目，等同于 @CacheEvict 注解。
     *
     * <cache:annotation-driven> 元素与 Java 配置中所对应的 @EnableCaching 非常类似，会启用注解驱动
     * 的缓存。
     *
     * 而另外的其他元素都用于基于 XML 的缓存配置。接下来的代码展现了如何使用这些元素为 SpittleRepository
     * bean 配置缓存，其作用等同于使用缓存注解的方式。
     *
     * <aop:config>
     *     <aop:advisor advice-ref="cacheAdvice"
     *          pointcut="execution(* com.habuma.spittr.db.SpittleRepository.*(..))" />
     * </aop:config>
     *
     * <cache:advice id="cacheAdvice">
     *     <cache:caching>
     *         <cache:cacheable cache="spittleCache" method="findRecent" />
     *         <cache:cacheable cache="spittleCache" method="findOne" />
     *         <cache:cacheable cache="spittleCache" method="findBySpitterId" />
     *         <cache:cache-put cache="spittleCache" method="save" key="#result.id" />
     *         <cache:cache-evict cache="spittleCache" method="delete" />
     *     </cache:caching>
     * </cache:advice>
     *
     * <bean id="cacheManager"
     *      class="org.springframework.cache.concurrent.ConcurrentMapCacheManager" />
     *
     * 这里首先看到的是 <aop:advisor>，它引用 ID 为 cacheAdvice 的通知，该元素将这个通知与一个切点进行
     * 匹配，因此建立了一个完整的切面。在本例中，这个切面的切点会在执行 SpittleRepository 的任意方法时触
     * 发。如果这样的方法被 Spring 应用上下文中的任意某个 bean 所调用，那么就会调用切面的通知。
     *
     * 在这里，通知利用 <cache:advice> 元素进行了声明。在 <cache:advice> 元素中，可以包含任意数量的
     * <cache:caching> 元素，这些元素用来完整地定义应用的缓存规则。
     *
     * 在本例中，只包含了一个 <cache:caching> 元素。这个元素又包含了三个 <cache:cacheable> 元素和一个
     * <cache:cache-put> 元素以及一个 <cache:cache-evict> 元素。
     *
     * 每个 <cache:cacheable> 元素都声明了切点中的某一个方法是支持缓存的。这是与 @Cacheable 注解同等作
     * 用的 XML 元素。具体来讲，findRecent()、findOne() 和 findBySpitterId() 都声明为支持缓存，它们
     * 的返回值将会保存在名为 spittleCache 的缓存之中。
     *
     * <cache:cache-put> 是 Spring XML 中与 @CachePut 注解同等作用的元素。它表明一个方法的返回值要填
     * 充到缓存之中，但是这个方法本身并不会从缓存中获取返回值。在本例中，save() 方法用来填充缓存。同面向注
     * 解的缓存一样，需要将默认的 key 改为返回 Spittle 对象的 id 属性。
     *
     * 最后，<cache:cache-evict> 元素是 Spring XML 中用来替代 @CacheEvict 注解的。它会从缓存中移除元
     * 素，这样的话，下次有人进行查找的时候就找不到了。在这里，调用 delete() 时，会将缓存中的 Spittle 删
     * 除掉，其中 key 与 delete() 方法所传递进来的 ID 参数相等的条目会从缓存中移除。
     *
     * 需要注意的是，<cache:advice> 元素有一个 cache-manager 元素，用来指定作为缓存管理器的 bean。它的
     * 默认值是 cacheManager，这与底部最后一个 <bean> 的声明恰好是一致的，所以没有必要再显式地进行设置。
     * 但是，如果缓存管理器的 ID 与之不同的话（使用多个缓存管理器的时候，可能会遇到这样的场景），那么可以通
     * 过设置 cache-manager 属性指定要使用哪个缓存管理器。
     *
     * 另外，还要留意的是，<cache:cacheable>、<cache:cache-put> 和 <cache:cache-evict> 元素都引用
     * 了同一个名为 spittleCache 的缓存。为了消除这种重复，可以在 <cache:caching> 元素上指明缓存的名字：
     *
     * <cache:advice id="cacheAdvice">
     *     <cache:caching cache="spittleCache">
     *         <cache:cacheable method="findRecent" />
     *         <cache:cacheable method="findOne" />
     *         <cache:cacheable method="findBySpitterId" />
     *         <cache:cache-put method="save" key="#result.id" />
     *         <cache:cache-evict method="delete" />
     *     </cache:caching>
     * </cache:advice>
     *
     * <cache:caching> 有几个可以供 <cache:cacheable>、<cache:cache-put> 和 <cache:cache-evict>
     * 共享的属性，包括：
     * （1）cache：指明要存储和获取值的缓存；
     * （2）condition：SpEL 表达式，如果计算得到的值为 false，将会为这个方法禁用缓存；
     * （3）key：SpEL 表达式，用来得到缓存的 key（默认为方法的参数）；
     * （4）method：要缓存的方法名。
     *
     * 除此之外，<cache:cacheable> 和 <cache:cache-put> 还有一个 unless 属性，可以为这个可选的属性指
     * 定一个 SpEL 表达式，如果这个表达式的计算结果为 true，那么将会阻止将返回值放到缓存之中。
     *
     * <cache:cache-evict> 元素还有几个特有的属性：
     * （1）all-entries：如果是 true 的话，缓存中所有的条目都会被移除掉。如果是 false 的话，只有匹配 key
     * 的条目才会被移除掉。
     * （2）before-invocation：如果是 true 的话，缓存条目将会在方法调用之前被移除掉。如果是 false 的话，
     * 方法调用之后才会移除缓存。
     *
     * all-entries 和 before-invocation 的默认值都是 false。这意味着在使用 <cache:cache-evict> 元
     * 素且不配置这两个属性时，会在方法调用完成后只删除一个缓存条目。要删除的条目会通过默认的 key（基于方法
     * 的参数）进行识别，当然也可以通过为名为 key 的属性设置一个 SpEL 表达式指定要删除的 key。
     */
    public static void main(String[] args) {

    }

}
