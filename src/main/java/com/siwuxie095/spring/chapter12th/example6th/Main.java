package com.siwuxie095.spring.chapter12th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-02-28 20:06:54
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 编写 MongoDB Repository
     *
     * 已经通过 @EnableMongoRepositories 注解启用了 Spring Data MongoDB 的 Repository 功能，接下来
     * 需要做的就是创建一个接口，Repository 实现要基于这个接口来生成。在这里，要扩展的是MongoRepository
     * 接口。如下代码中的 OrderRepository 扩展了 MongoRepository，为 Order 文档提供了基本的 CRUD 操作。
     *
     * public interface OrderRepository extends MongoRepository<Order, String> {
     *
     * }
     *
     * 因为 OrderRepository 扩展了 MongoRepository，因此它就会传递性地扩展 Repository 标记接口。任何扩展
     * Repository 的接口将会在运行时自动生成实现。在这里，并不会实现与关系型数据库交互的 JPA Repository，而
     * 是会为 OrderRepository 生成读取和写入数据到 MongoDB 文档数据库的实现。
     *
     * MongoRepository 接口有两个参数，第一个是带有 @Document 注解的对象类型，也就是该 Repository 要处理
     * 的类型。第二个参数是带有 @Id 注解的属性类型。
     *
     * 尽管 OrderRepository 本身并没有定义任何方法，但是它会继承多个方法，包括对 Order 文档进行 CRUD 操作的
     * 方法。如下描述了 OrderRepository 继承的所有方法。
     * （1）long count()：返回指定 Repository 类型的文档数量；
     * （2）void delete(Iterable<? extends T>)：删除与指定对象关联的所有文档；
     * （3）void delete(T)：删除与指定对象关联的文档；
     * （4）void delete(ID)：根据 ID 删除某一个文档；
     * （5）void deleteAll()：删除指定 Repository 类型的所有文档；
     * （6）boolean exists(Object)：如果存在与指定对象相关联的文档，则返回 true；
     * （7）boolean exists(ID)：如果存在指定 ID 的文档，则返回 true；
     * （8）List<T> findAll()：返回指定 Repository 类型的所有文档；
     * （9）List<T> findAll(Iterable<ID>)：返回指定文档 ID 对应的所有文档；
     * （10）List<T> findAll(Pageable)：为指定的 Repository 类型，返回分页且排序的文档列表；
     * （11）List<T> findAll(Sort)：为指定的 Repository 类型，返回排序后的所有文档列表；
     * （12）T findOne(ID)：为指定的 ID 返回单个文档；
     * （13）Iterable<S> save(Iterable<s>)：保存指定 Iterable 中的所有文档；
     * （14）S save(<S>)：为给定的对象保存一条文档。
     *
     * 这些方法使用了传递进来和方法返回的泛型。OrderRepository 扩展了 MongoRepository<Order, String>，
     * 那么 T 就映射为 Order，ID 映射为 String，而 S 映射为所有扩展 Order 的类型。
     *
     *
     *
     * 1、添加自定义的查询方法
     *
     * 通常来讲，CRUD 操作是很有用的，但有时候可能希望 Repository 提供除内置方法以外的其他方法。
     *
     * Spring Data JPA 支持方法命名约定，它能够帮助 Spring Data 为遵循约定的方法自动生成实现。实际上，相同
     * 的约定也适用于 Spring Data MongoDB。这意味着可以为 OrderRepository 添加自定义的方法：
     *
     * public interface OrderRepository extends MongoRepository<Order, String> {
     *
     *     List<Order> findByCustomer(String customer);
     *
     *     List<Order> findByCustomerLike(String customer);
     *
     *     List<Order> findByCustomerAndType(String customer, String type);
     *
     *     List<Order> findByCustomerLikeAndType(String customer, String type);
     *
     * }
     *
     * 这里有四个新的方法，每一个都是查找满足特定条件的 Order 对象。其中：
     * （1）第一个方法用来获取 customer 属性等于传入值的 Order 列表；
     * （2）第二个方法获取 customer 属性 like 传入值的 Order 列表；
     * （3）第三个方法会返回 customer 和 type 属性等于传入值的 Order 列表；
     * （4）最后一个方法与前一个类似，只不过 customer 在对比的时候使用的是 like 而不是 equals。
     *
     * 其中，find 这个查询动词并不是固定的。如果喜欢的话，还可以使用 get 作为查询动词：
     *
     * List<Order> getByCustomer(String customer);
     *
     * 如果 read 更适合的话，你还可以使用这个动词：
     *
     * List<Order> readByCustomer(String customer);
     *
     * 除此之外，还有一个特殊的动词用来为匹配的对象计数：
     *
     * int countByCustomer(String customer);
     *
     * 与 Spring Data JPA 类似，在查询动词与 By 之前，有很大的灵活性。例如，可以标示要查找什么内容：
     *
     * List<Order> findOrdersByCustomer(String customer);
     *
     * 其中，Orders 这个词没并没有什么特殊之处，它不会影响要获取的内容。也可以将方法按照如下的方式命名：
     *
     * List<Order> findSomeStaffWeNeedByCustomer(String customer);
     *
     * 其实，并不是必须要返回 List<Order>，如果只想要一个 Order 对象的话，可以只需简单地返回 Order：
     *
     * Order findASingleOrderByCustomer(String customer);
     *
     * 这里，所返回的就是原本 List 中的第一个 Order 对象。如果没有匹配元素的话，方法将会返回 null。
     *
     *
     *
     * 2、指定查询
     *
     * @Query 能够像在 JPA 中那样用在 MongoDB 上。唯一的区别在于针对 MongoDB 时，@Query 会接受一个 JSON
     * 查询，而不是 JPA 查询。
     *
     * 例如，假设想要查询给定类型的订单，并且要求 customer 的名称为 "Chuck Wagon"。OrderRepository 中如下
     * 的方法声明能够完成所需的任务：
     *
     *     @Query("{'customer' : 'Chuck Wagon', 'type': ?0}")
     *     List<Order> findChucksOrders(String type);
     *
     * @Query 中给定的 JSON 将会与所有的 Order 文档进行匹配，并返回匹配的文档。需要注意的是，type 属性映射成
     * 了 "?0"，这表明 type 属性应该与查询方法的第零个参数相等。如果有多个参数的话，它们可以通过 "?1"、"?2"
     * 等方式进行引用。
     *
     *
     *
     * 3、混合自定义的功能
     *
     * 前面学习了如何将完全自定义的方法混合到自动生成的 Repository 中。对于 JPA 来说，这还涉及到创建一个中间接口来
     * 声明自定义的方法，为这些自定义方法创建实现类并修改自动化的 Repository 接口，使其扩展中间接口。对于 Spring
     * Data MongoDB 来说，这些步骤都是相同的。
     *
     * 假设想要查询文档中 type 属性匹配给定值的 Order 对象。可以通过创建签名为 List<Order> findByType(String t)
     * 的方法，很容易实现这个功能。但是，如果给定的类型是 "NET"，那就查找 type 值为 "WEB" 的 Order 对象。要实现
     * 这个功能的话，这就有些困难了，即便使用 @Query 注解也不容易实现。不过，混合实现的做法能够完成这项任务。
     *
     * 首先，定义中间接口：
     *
     * public interface OrderOperations {
     *
     *     List<Order> findOrdersByType(String type);
     *
     * }
     *
     * 这非常简单。接下来，是编写混合实现：
     *
     * public class OrderRepositoryImpl implements OrderOperations {
     *
     *     @Autowired
     *     private MongoOperations mongo;
     *
     *     @Override
     *     public List<Order> findOrdersByType(String type) {
     *         String newType = type.equals("NET") ? "WEB" : type;
     *         Criteria criteria = Criteria.where("type").is(newType);
     *         Query query = Query.query(criteria);
     *         return mongo.find(query, Order.class);
     *     }
     *
     * }
     *
     * 可以看到，混合实现中注入了 MongoOperations（也就是 MongoTemplate 所实现的接口）。findOrdersByType()
     * 方法使用 MongoOperations 对数据库进行了查询，查找匹配条件的文档。
     *
     * 剩下的工作就是修改 OrderRepository，让其扩展中间接口 OrderOperations：
     *
     * public interface OrderRepository extends MongoRepository<Order, String>, OrderOperations {
     *
     * ...
     *
     * }
     *
     * 将这些关联起来的关键点在于实现类的名称为 OrderRepositoryImpl。这个名字前半部分与 OrderRepository 相同，
     * 只是添加了 "Impl" 后缀。当 Spring Data MongoDB 生成 Repository 实现时，它会查找这个类并将其混合到自动
     * 生成的实现中。
     *
     * 如果你不喜欢 "Impl" 后缀的话，那么可以配置 Spring Data MongoDB，让其按照名字查找具备不同后缀的类。这里
     * 需要做的就是设置 @EnableMongoRepositories 的属性（在 Spring 配置类中）：
     *
     * @Configuration
     * @EnableMongoRepositories(basePackages = "com.siwuxie095.spring.chapter12th.example6th.db",
     * repositoryImplementationPostfix = "Staff")
     * public class MongoConfig extends AbstractMongoConfiguration {
     *
     * ...
     *
     * }
     *
     * 如果使用 XML 配置的话，可以设置 <mongo:repositories> 的 repository-impl-postfix 属性：
     *
     * <mongo:repositories base-package="com.siwuxie095.spring.chapter12th.example6th.db"
     *                     repository-impl-postfix="Staff" />
     *
     * 不管采用哪种方式，现在都让 Spring Data MongoDB 查找名为 OrderRepositoryStuff 的类，而不再
     * 查找 OrderRepositoryImpl。
     *
     * 像 MongoDB 这样的文档数据库能够解决特定类型的问题，但是就像关系型数据库不是全能型数据库那样，MongoDB 同样
     * 如此。有些问题并不是关系型数据库或文档型数据库适合解决的，不过，幸好现有的选择并不仅限于这两种。
     *
     * 后续将会介绍 Spring Data 如何支持 Neo4j，这是一种很流行的图数据库。
     */
    public static void main(String[] args) {

    }

}
