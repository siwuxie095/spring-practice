package com.siwuxie095.spring.chapter13th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-03-06 18:07:58
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 为方法添加注解以支持缓存
     *
     * Spring 的缓存抽象在很大程度上是围绕切面构建的。在 Spring 中启用缓存时，会创建一个切面，它触发一个或更多
     * 的 Spring 的缓存注解。如下列出了 Spring 所提供的缓存注解。
     * （1）@Cacheable：表明 Spring 在调用方法之前，首先应该在缓存中查找方法的返回值。如果这个值能够找到，就会
     * 返回缓存的值。否则的话，这个方法就会被调用，返回值会放到缓存之中。
     * （2）@CachePut：表明 Spring 应该将方法的返回值放到缓存中。在方法的调用前并不会检查缓存，方法始终都会被
     * 调用。
     * （3）@CacheEvict：表明 Spring 应该在缓存中清除一个或多个条目。
     * （4）@Caching：这是一个分组的注解，能够同时应用多个其他的缓存注解。
     *
     * 上面的的所有注解都能运用在方法或类上。当将其放在单个方法上时，注解所描述的缓存行为只会运用到这个方法上。
     * 如果注解放在类级别的话，那么缓存行为就会应用到这个类的所有方法上。
     *
     *
     *
     * 1、填充缓存
     *
     * 可以看到，@Cacheable 和 @CachePut 注解都可以填充缓存，但是它们的工作方式略有差异。
     *
     * @Cacheable 首先在缓存中查找条目，如果找到了匹配的条目，那么就不会对方法进行调用了。如果没有找到匹配的条
     * 目，方法会被调用并且返回值要放到缓存之中。而 @CachePut 并不会在缓存中检查匹配的值，目标方法总是会被调用，
     * 并将返回值添加到缓存之中。
     *
     * @Cacheable 和 @CachePut 有一些属性是共有的：
     * （1）
     * 属性：value
     * 类型：String[]
     * 描述：要使用的缓存名称
     * （2）
     * 属性：condition
     * 类型：String
     * 描述：SpEL 表达式，如果得到的值是 false 的话，不会将缓存应用到方法调用上
     * （3）
     * 属性：key
     * 类型：String
     * 描述：SpEL 表达式，用来计算自定义的缓存 key
     * （4）
     * 属性：unless
     * 类型：String
     * 描述：SpEL 表达式，如果得到的值是 true 的话，返回值不会放到缓存之中
     *
     * 在最简单的情况下，在 @Cacheable 和 @CachePut 的这些属性中，只需使用 value 属性指定一个或多个缓存即可。
     * 例如，考虑 SpittleRepository 的 findOne() 方法。在初始保存之后，Spittle 就不会再发生变化了。如果有
     * 的 Spittle 比较热门并且会被频繁请求，反复地在数据库中进行获取是对时间和资源的浪费。通过在 findOne() 方
     * 法上添加 @Cacheable 注解，如下所示，能够确保将 Spittle 保存在缓存中，从而避免对数据库的不必要访问。
     *
     *     @Cacheable("spittleCache")
     *     public Spittle findOne(long id) {
     *         try {
     *             return jdbcTemplate.queryForObject(SELECT_SPITTLE_BY_ID, new SpittleRowMapper(), id);
     *         } catch (EmptyResultDataAccessException e) {
     *             return null;
     *         }
     *     }
     *
     * 当 findOne() 被调用时，缓存切面会拦截调用并在缓存中查找之前以名 spittleCache 存储的返回值。缓存的 key
     * 是传递到 findOne() 方法中的 id 参数。如果按照这个 key 能够找到值的话，就会返回找到的值，方法不会再被调
     * 用。如果没有找到值的话，那么就会调用这个方法，并将返回值放到缓存之中，为下一次调用 findOne() 方法做好准备。
     *
     * 在这里，@Cacheable 注解被放到了 JdbcSpittleRepository 的 findOne() 方法实现上。这样能够起作用，但是
     * 缓存的作用只限于 JdbcSpittleRepository 这个实现类中，SpittleRepository 的其他实现并没有缓存功能，除
     * 非也为其添加上 @Cacheable 注解。因此，可以考虑将注解添加到 SpittleRepository 的方法声明上，而不是放在
     * 实现类中：
     *
     *     @Cacheable("spittleCache")
     *     Spittle findOne(long id);
     *
     * 当为接口方法添加注解后，@Cacheable 注解会被 SpittleRepository 的所有实现继承，这些实现类都会应用相同
     * 的缓存规则。
     *
     *
     * 1.1、将值放到缓存之中
     *
     * @Cacheable 会条件性地触发对方法的调用，这取决于缓存中是不是已经有了所需要的值，对于所注解的方法，@CachePut
     * 采用了一种更为直接的流程。带有 @CachePut 注解的方法始终都会被调用，而且它的返回值也会放到缓存中。这提供一种
     * 很便利的机制，能够在请求之前预先加载缓存。
     *
     * 例如，当一个全新的 Spittle 通过 SpittleRepository 的 save() 方法保存之后，很可能马上就会请求这条记录。
     * 所以，当 save() 方法调用后，立即将 Spittle 塞到缓存之中是很有意义的，这样当其他人通过 findOne() 对其进行
     * 查找时，它就已经准备就绪了。为了实现这一点，可以在 save() 方法上添加 @CachePut 注解，如下所示：
     *
     *     @CachePut(value = "spittleCache")
     *     Spittle save(Spittle spittle);
     *
     * 当 save() 方法被调用时，它首先会做所有必要的事情来保存 Spittle，然后返回的 Spittle 会被放到 spittleCache
     * 缓存中。
     *
     * 在这里只有一个问题：缓存的 key。默认的缓存 key 要基于方法的参数来确定。因为 save() 方法的唯一参数就是 Spittle，
     * 所以它会用作缓存的 key。将 Spittle 放在缓存中，而它的缓存 key 恰好是同一个 Spittle，这是不是有一点诡异呢？
     *
     * 显然，在这个场景中，默认的缓存 key 并不是想要的。这里需要的缓存 key 是新保存 Spittle 的 ID，而不是 Spittle
     * 本身。所以，在这里需要指定一个 key 而不是使用默认的 key。下面看一下怎样自定义缓存 key。
     *
     *
     * 1.2、自定义缓存 key
     *
     * @Cacheable 和 @CachePut 都有一个名为 key 属性，这个属性能够替换默认的 key，它是通过一个 SpEL 表达式计算
     * 得到的。任意的 SpEL 表达式都是可行的，但是更常见的场景是所定义的表达式与存储在缓存中的值有关，据此计算得到 key。
     *
     * 具体到这个场景，需要将 key 设置为所保存 Spittle 的 ID。以参数形式传递给 save() 的 Spittle 还没有保存，因
     * 此并没有 ID。只能通过 save() 返回的 Spittle 得到 id 属性。
     *
     * 幸好，在为缓存编写 SpEL 表达式的时候，Spring 暴露了一些很有用的元数据。如下列出了 SpEL 中可用的缓存元数据。
     * （1）#root.args：传递给缓存方法的参数，形式为数组；
     * （2）#root.caches：该方法执行时所对应的缓存，形式为数组；
     * （3）#root.target：目标对象；
     * （4）#root.targetClass：目标对象的类，是 #root.target.class 的简写形式；
     * （5）#root.method：缓存方法；
     * （6）#root.methodName：缓存方法的名字，是 #root.method.name 的简写形式；
     * （7）#result：方法调用的返回值（不能用在 @Cacheable 注解上）；
     * （8）#Argument：任意的方法参数名（如 #argName）或参数索引（如 #a0 或 #p0）。
     *
     * 对于 save() 方法来说，需要的键是所返回 Spittle 对象的 id 属性。表达式 #result 能够得到返回的 Spittle。
     * 借助这个对象，可以通过将 key 属性设置为 #result.id 来引用 id 属性：
     *
     *     @CachePut(value = "spittleCache", key = "#result.id")
     *     Spittle save(Spittle spittle);
     *
     * 按照这种方式配置 @CachePut，缓存不会去干涉 save() 方法的执行，但是返回的 Spittle 将会保存在缓存中，并且
     * 缓存的 key 与 Spittle 的 id 属性相同。
     *
     *
     * 1.3、条件化缓存
     *
     * 通过为方法添加 Spring 的缓存注解，Spring 就会围绕着这个方法创建一个缓存切面。但是，在有些场景下可能希望将
     * 缓存功能关闭。
     *
     * @Cacheable 和 @CachePut 提供了两个属性用以实现条件化缓存：unless 和 condition，这两个属性都接受一个
     * SpEL 表达式。如果 unless 属性的 SpEL 表达式计算结果为 true，那么缓存方法返回的数据就不会放到缓存中。与
     * 之类似，如果 condition 属性的 SpEL 表达式计算结果为 false，那么对于这个方法缓存就会被禁用掉。
     *
     * 表面上来看，unless 和 condition 属性做的是相同的事情。但是，这里有一点细微的差别。unless 属性只能阻止
     * 将对象放进缓存，但是在这个方法调用的时候，依然会去缓存中进行查找，如果找到了匹配的值，就会返回找到的值。与
     * 之不同，如果 condition 的表达式计算结果为 false，那么在这个方法调用的过程中，缓存是被禁用的。就是说，不
     * 会去缓存进行查找，同时返回值也不会放进缓存中。
     *
     * 作为样例（尽管有些牵强），假设对于 message 属性包含 "NoCache" 的 Spittle 对象，就对其进行缓存。为了阻止
     * 这样的 Spittle 对象被缓存起来，可以这样设置 unless 属性：
     *
     *     @Cacheable(value = "spittleCache", unless = "#result.message.contains('NoCache')")
     *     Spittle save(Spittle spittle);
     *
     * 为 unless 设置的 SpEL 表达式会检查返回的 Spittle 对象（在表达式中通过 #result 来识别）的 message 属性。
     * 如果它包含 "NoCache" 文本内容，那么这个表达式的计算值为 true，这个 Spittle 对象不会放进缓存中。否则的话，
     * 表达式的计算结果为 false，无法满足 unless 的条件，这个 Spittle 对象会被缓存。
     *
     * 属性 unless 能够阻止将值写入到缓存中，但是有时候希望将缓存全部禁用。也就是说，在一定的条件下，既不希望将值
     * 添加到缓存中，也不希望从缓存中获取数据。
     *
     * 例如，对于 ID 值小于 10 的 Spittle 对象，不希望对其使用缓存。在这种场景下，这些 Spittle 是用来进行调试的
     * 测试条目，对其进行缓存并没有实际的价值。为了要对 ID 小于 10 的 Spittle 关闭缓存，可以在 @Cacheable 上使
     * 用 condition 属性，如下所示：
     *
     *     @Cacheable(value = "spittleCache",
     *          unless = "#result.message.contains('NoCache')",
     *          condition="#id >= 10")
     *     Spittle save(Spittle spittle);
     *
     * 如果 findOne() 调用时，参数值小于 10，那么将不会在缓存中进行查找，返回的 Spittle 也不会放进缓存中，就像
     * 这个方法没有添加 @Cacheable 注解一样。
     *
     * 如样例所示，unless 属性的表达式能够通过 #result 引用返回值。这是很有用的，这么做之所以可行是因为 unless
     * 属性只有在缓存方法有返回值时才开始发挥作用。而 condition 肩负着在方法上禁用缓存的任务，因此它不能等到方法
     * 返回时再确定是否该关闭缓存。这意味着它的表达式必须要在进入方法时进行计算，所以不能通过 #result 引用返回值。
     *
     * 现在已经在缓存中添加了内容，但是这些内容能被移除掉吗？接下来看一下如何借助 @CacheEvict 将缓存数据移除掉。
     *
     *
     *
     * 2、移除缓存条目
     *
     * @CacheEvict 并不会往缓存中添加任何东西。相反，如果带有 @CacheEvict 注解的方法被调用的话，那么会有一个
     * 或更多的条目会在缓存中移除。
     *
     * 那么在什么场景下需要从缓存中移除内容呢？当缓存值不再合法时，应该确保将其从缓存中移除，这样的话，后续的缓存
     * 命中就不会返回旧的或者已经不存在的值，其中一个这样的场景就是数据被删除掉了。这样的话，SpittleRepository
     * 的 delete() 方法就是使用 @CacheEvict 的绝佳选择：
     *
     *     @CacheEvict("spittleCache")
     *     void delete(long id);
     *
     * 注意：与 @Cacheable 和 @CachePut 不同，@CacheEvict 能够应用在返回值为 void 的方法上， 而 @Cacheable
     * 和 @CachePut 需要非 void 的返回值，它将会作为放在缓存中的条目。因为 @CacheEvict 只是将条目从缓存中移除，
     * 因此它可以放在任意的方法上，甚至 void 方法。
     *
     * 从这里可以看到，当 delete() 调用时，会从缓存中删除一个条目。被删除条目的 key 与传递进来的 spittleId 参数
     * 的值相等。
     *
     * @CacheEvict 有多个属性，如下所示，这些属性会影响到该注解的行为，使其不同于默认的做法。
     * （1）
     * 属性：value
     * 类型：String[]
     * 描述：要使用的缓存名称
     * （2）
     * 属性：key
     * 类型：String
     * 描述：SpEL 表达式，用来计算自定义的缓存 key
     * （3）
     * 属性：condition
     * 类型：String
     * 描述：SpEL 表达式，如果得到的值是 false 的话，缓存不会应用到方法调用上
     * （4）
     * 属性：allEntries
     * 类型：boolean
     * 描述：如果为 true 的话，特定缓存的所有条目都会被移除掉
     * （5）
     * 属性：beforeInvocation
     * 类型：boolean
     * 描述：如果为 true 的话，在方法调用之前移除条目。如果为 false（默 认值）的话，在方法成功调用之后再移除条目
     */
    public static void main(String[] args) {

    }

}
