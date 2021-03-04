package com.siwuxie095.spring.chapter12th.example14th;

/**
 * @author Jiajing Li
 * @date 2021-03-03 22:27:47
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 RedisTemplate
     *
     * 顾名思义，Redis 连接工厂会生成到 Redis key-value 存储的连接（以 RedisConnection 的形式）。借助
     * RedisConnection，可以存储和读取数据。例如，可以获取连接并使用它来保存一个问候信息，如下所示：
     *
     * RedisConnectionFactory cf = new JedisConnectionFactory();
     * RedisConnection conn = cf.getConnection();
     * conn.set("greeting".getBytes(), "Hello World".getBytes());
     *
     * 与之类似，还可以使用 RedisConnection 来获取之前存储的问候信息：
     *
     * byte[] greetingBytes = conn.get("greeting".getBytes());
     * String greeting = new String(greetingBytes);
     *
     * 毫无疑问，这可以正常运行，但是你难道真的愿意使用字节数组吗？
     *
     * 与其他的 Spring Data 项目类似，Spring Data Redis 以模板的形式提供了较高等级的数据访问方案。实际
     * 上，Spring Data Redis 提供了两个模板：
     * （1）RedisTemplate
     * （2）StringRedisTemplate
     *
     * RedisTemplate 可以极大地简化 Redis 数据访问，能够持久化各种类型的 key 和 value，并不局限于字节数
     * 组。在认识到 key 和 value 通常是 String 类型之后，StringRedisTemplate 扩展了 RedisTemplate，
     * 只关注 String 类型。
     *
     * 假设已经有了 RedisConnectionFactory，那么可以按照如下的方式构建 RedisTemplate：
     *
     * RedisConnectionFactory cf = new JedisConnectionFactory();
     * RedisTemplate<String, Product> redis = new RedisTemplate<String, Product>();
     * redis.setConnectionFactory(cf);
     *
     * 注意，RedisTemplate 使用两个类型进行了参数化。第一个是 key 的类型，第二个是 value 的类型。在这里
     * 所构建的 RedisTemplate 中，将会保存 Product 对象作为 value，并将其赋予一个 String 类型的 key。
     *
     * 如果你所使用的 value 和 key 都是 String 类型，那么可以考虑使用 StringRedisTemplate 来代替
     * RedisTemplate：
     *
     * RedisConnectionFactory cf = new JedisConnectionFactory();
     * StringRedisTemplate redis = new StringRedisTemplate(cf);
     *
     * 注意，与 RedisTemplate 不同，StringRedisTemplate 有一个接受 RedisConnectionFactory 的构造器，
     * 因此没有必要在构建后再调用 setConnectionFactory()。
     *
     * 尽管这并非必须的，但是如果你经常使用 RedisTemplate 或 StringRedisTemplate 的话，你可以考虑将其
     * 配置为 bean，然后注入到需要的地方。如下就是一个声明 RedisTemplate 的简单 @Bean 方法：
     *
     *     @Bean
     *     public RedisTemplate<String, Product> redisTemplate(RedisConnectionFactory cf) {
     *         RedisTemplate<String, Product> redis = new RedisTemplate<String, Product>();
     *         redis.setConnectionFactory(cf);
     *         return redis;
     *     }
     *
     * 如下是声明 StringRedisTemplate bean 的 @Bean 方法：
     *
     *     @Bean
     *     public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory cf) {
     *         return new StringRedisTemplate(cf);
     *     }
     *
     * 有了 RedisTemplate（或 StringRedisTemplate）之后，就可以开始保存、获取以及删除 key-value 条目
     * 了。RedisTemplate 的大多数操作都是如下的子 API 提供的。
     * （1）
     * 方法：opsForValue()
     * 子 API 接口：ValueOperations<K, V>
     * 描述：操作具有简单值的条目
     * （2）
     * 方法：opsForList()
     * 子 API 接口：ListOperations<K, V>
     * 描述：操作具有 list 值的条目
     * （3）
     * 方法：opsForSet()
     * 子 API 接口：SetOperations<K, V>
     * 描述：操作具有 set 值的条目
     * （4）
     * 方法：opsForZSet()
     * 子 API 接口：ZSetOperations<K, V>
     * 描述：操作具有 ZSet 值（排序的 set）的条目
     * （5）
     * 方法：opsForHash()
     * 子 API 接口：HashOperations<K, HK, HV>
     * 描述：操作具有 hash 值的条目
     * （6）
     * 方法：boundValueOps(K)
     * 子 API 接口：BoundValueOperations<K,V>
     * 描述：以绑定指定 key 的方式，操作具有简单值的条目
     * （7）
     * 方法：boundListOps(K)
     * 子 API 接口：BoundListOperations<K,V>
     * 描述：以绑定指定 key 的方式，操作具有 list 值的条目
     * （8）
     * 方法：boundSetOps(K)
     * 子 API 接口：BoundSetOperations<K,V>
     * 描述：以绑定指定 key 的方式，操作具有 set 值的条目
     * （9）
     * 方法：boundZSet(K)
     * 子 API 接口：BoundZSetOperations<K,V>
     * 描述：以绑定指定 key 的方式，操作具有 ZSet 值（排序的 set）的条目
     * （10）
     * 方法：boundHashOps(K)
     * 子 API 接口：BoundHashOperations<K,V>
     * 描述：以绑定指定 key 的方式，操作具有 hash 值的条目
     *
     * PS：RedisTemplate 的很多功能是以子 API 的形式提供的，它们区分了单个值和集合值的场景。
     *
     * 可以看到，这些子 API 能够通过 RedisTemplate（和 StringRedisTemplate）进行调用。其中每个子 API
     * 都提供了使用数据条目的操作，基于 value 中所包含的是单个值还是一个值的集合它们会有所差别。
     *
     * 这些子 API 中，包含了很多从 Redis 中存取数据的方法。这里不会介绍所有的方法，但是会介绍一些最为常用
     * 的操作。
     *
     *
     *
     * 1、使用简单的值
     *
     * 假设想通过 RedisTemplate<String, Product> 保存 Product，其中 key 是 sku 属性的值。如下的代码
     * 片段展示了如何借助 opsForValue() 方法完成该功能：
     *
     * redis.opsForValue().set(product.getSku(), product);
     *
     * 类似地，如果你希望获取 sku 属性为 123456 的产品，那么可以使用如下的代码片段：
     *
     * Product product = redis.opsForValue().get("123456");
     *
     * 如果按照给定的 key，无法获得条目的话，将会返回 null。
     *
     *
     *
     * 2、使用 List 类型的值
     *
     * 使用 List 类型的 value 与之类似，只需使用 opsForList() 方法即可。例如，可以在一个 List 类型的条
     * 目尾部添加一个值：
     *
     * redis.opsForList().rightPush("cart", product);
     *
     * 通过这种方式，向列表的尾部添加了一个 Product，所使用的这个列表在存储时 key 为 cart。如果这个 key
     * 尚未存在列表的话，将会创建一个。
     *
     * rightPush() 会在列表的尾部添加一个元素，而 leftPush() 则会在列表的头部添加一个值：
     *
     * redis.opsForList().leftPush("cart", product);
     *
     * 有很多方式从列表中获取元素，可以通过 leftPop() 或 rightPop() 方法从列表中弹出一个元素：
     *
     * Product first = redis.opsForList().leftPop("cart");
     * Product last = redis.opsForList().rightPop("cart");
     *
     * 除了从列表中获取值以外，这两个方法还有一个副作用就是从列表中移除所弹出的元素。如果你只是想获取值的话
     * （甚至可能要在列表的中间获取），那么可以使用 range() 方法：
     *
     * List<Product> products = redis.opsForList().range("cart", 2, 12);
     *
     * range() 方法不会从列表中移除任何元素，但是它会根据指定的 key 和索引范围，获取范围内的一个或多个值。
     * 前面的样例中，会获取 11 个元素，从索引为 2 的元素到索引为 12 的元素（不包含）。如果范围超出了列表
     * 的边界，那么只会返回索引在范围内的元素。如果该索引范围内没有元素的话，将会返回一个空的列表。
     *
     *
     *
     * 3、在 Set 上执行操作
     *
     * 除了操作列表以外，还可以使用 opsForSet() 操作 Set。最为常用的操作就是向 Set 中添加一个元素：
     *
     * redis.opsForSet().add("cart", product);
     *
     * 在有多个 Set 并填充值之后，就可以对这些 Set 进行一些有意思的操作，如获取其差异、求交集和求并集：
     *
     * List<Product> diff = redis.opsForSet().difference("cart1", "cart2");
     * List<Product> union = redis.opsForSet().union("cart1", "cart2");
     * List<Product> isect = redis.opsForSet().isect("cart1", "cart2");
     *
     * 当然，还可以移除它的元素：
     *
     * redis.opsForSet().remove(product);
     *
     * 甚至还可以随机获取 Set 中的一个元素：
     *
     * Product random = redis.opsForSet().randomMember("cart");
     *
     * 因为 Set 没有索引和内部的排序，因此无法精准定位某个点，然后从 Set 中获取元素。
     *
     *
     *
     * 4、绑定到某个 key 上
     *
     * 上面列出的子 API 中有五个子 API，它们能够以绑定 key 的方式执行操作。这些子 API 与其他的 API 是对
     * 应的，但是关注于某一个给定的 key。
     *
     * 为了举例阐述这些子 API 的用法，假设将 Product 对象保存到一个 list 中，并且 key 为 cart。在这种
     * 场景下，假设想从 list 的右侧弹出一个元素，然后在 list 的尾部新增三个元素。此时可以使用 boundListOps()
     * 方法所返回的 BoundListOperations：
     *
     * BoundListOperations<String, Product> cart = redis.boundListOps("cart");
     * Product poped = cart.rightPop();
     * cart.rightPush(product1);
     * cart.rightPush(product2);
     * cart.rightPush(product3);
     *
     * 注意，只在一个地方使用了条目的 key，也就是调用 boundListOps() 的时候。对返回的 BoundListOperations
     * 执行的所有操作都会应用到这个 key 上。
     */
    public static void main(String[] args) {

    }

}
