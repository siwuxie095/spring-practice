package com.siwuxie095.spring.chapter12th.example11th;

/**
 * @author Jiajing Li
 * @date 2021-03-02 07:57:47
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 创建自动化的 Neo4j Repository
     *
     * 大多数 Spring Data 项目都具备的最棒的一项功能就是为 Repository 接口自动生成实现。已经在 Spring
     * Data JPA 和 Spring Data MongoDB 中看到了这项功能。Spring Data Neo4j 也不例外，它同样支持
     * Repository 自动化生成功能。
     *
     * 已经将 @EnableNeo4jRepositories 添加到了配置中，所以 Spring Data Neo4j 已经配置为支持自动化
     * 生成 Repository的功能。这里所需要做的就是编写接口，如下的 OrderRepository 就是很好的起点：
     *
     * public interface OrderRepository extends GraphRepository<Order> {
     *
     * }
     *
     * 与其他的 Spring Data 项目一样，Spring Data Neo4j 会为扩展 Repository 接口的其他接口生成
     * Repository 方法实现。在本例中，OrderRepository 扩展了 GraphRepository，而后者又间接扩展
     * 了 Repository 接口。因此，Spring Data Neo4j 将会在运行时创建 OrderRepository 的实现。
     *
     * 注意，GraphRepository 使用 Order 进行了参数化，也就是这个 Repository 所要使用的实体类型。因为
     * Neo4j 要求图 ID 的类型为 Long，因此在扩展 GraphRepository 的时候，没有必要再去指定 ID 类型。
     *
     * 现在，就能够使用很多通用的 CRUD 操作了，这与 JpaRepository 和 MongoRepository 所提供的功能类
     * 似。如下描述了扩展 GraphRepository 所能够得到的方法。
     * （1）long count()：返回在数据库中，目标类型有多少实体；
     * （2）void delete(Iterable<?extendsT>)：删除多个实体；
     * （3）void delete(Long id)：根据 ID，删除一个实体；
     * （4）void delete(T)：删除一个实体；
     * （5）void deleteAll()：删除目标类型的所有实体；
     * （6）boolean exists(Long id)：根据指定的 ID，检查实体是否存在；
     * （7）EndResult<T> findAll()：获取目标类型的所有实体；
     * （8）Iterable<T> findAll(Iterable<Long>)：根据给定的 ID，获取目标类型的实体；
     * （9）Page<T> findAll(Pageable)：返回目标类型分页和排序后的实体列表；
     * （10）EndResult<T> findAll(Sort)：返回目标类型排序后的实体列表；
     * （11）EndResult<T> findAllBySchemaPropertyValue(String,Object)：返回指定属性匹配给定值的
     * 所有实体；
     * （12）Iterable<T> findAllByTraversal(N,TraversalDescription)：返回从某个节点开始，图遍历
     * 到达的节点；
     * （13）T findBySchemaPropertyValue(String,Object)：返回指定属性匹配给定值的一个实体；
     * （14）T findOne(Long)：根据 ID，获得某一个实体；
     * （15）EndResult<T> query(String,Map<String,Object>)：返回匹配给定 Cypher 查询的所有实体；
     * （16）Iterable<T> save(Iterable<T>)：保存多个实体；
     * （17）S save(S)：保存一个实体。
     *
     * PS：通过扩展 GraphRepository，Repository 接口能够继承多个 CRUD 操作，它们会由 Spring Data
     * Neo4j 自动实现。
     *
     * 这里不会介绍所有的方法，但是有些方法你可能会经常用到。例如，如下的代码能够保存一个 Order 实体：
     *
     * Order savedOrder = orderRepository.save(order);
     *
     * 当实体保存之后，save() 方法将会返回被保存的实体，如果之前它使用 @GraphId 注解的属性值为 null 的
     * 话，此时这个属性将会填充上值。
     *
     * 还可以使用 findOne() 方法查询某一个实体。例如，下面的这行代码将会查询图 ID 为 4 的 Order：
     *
     * Order order = orderRepository.findOne(4L);
     *
     * 还可以查询所有的 Order：
     *
     * EndResult<Order> allOrders = orderRepository.findAll();
     *
     * 当然，你可能还希望删除某一个实体。这种情况下，可以使用 delete() 方法：
     *
     * orderRepository.delete(order);
     *
     * 这将会从数据库中删除给定的 Order 节点。如果你只有图 ID 的话，那可以将其传递到 delete() 方法中，
     * 而不是再使用节点类型本身：
     *
     * orderRepository.delete(orderId);
     *
     * 如果你希望进行自定义的查询，那么可以使用 query() 方法对数据库执行任意的 Cypher 查询。但是这与使用
     * Neo4jTemplate 的 query() 方法并没有太大的差别。其实，还可以为 OrderRepository 添加自定义的查
     * 询方法。
     *
     *
     *
     * 1、添加查询方法
     *
     * 已经看过如何按照命名约定使用 Spring Data JPA 和 Spring Data MongoDB 来添加自定义的查询方法。
     * 如果 Spring Data Neo4j 没有提供相同功能的话，那就该失望了。
     *
     * 如下面的代码所示，其实完全没有必要失望：
     *
     * public interface OrderRepository extends GraphRepository<Order> {
     *
     *     List<Order> findByCustomer(String customer);
     *
     *     List<Order> findByCustomerAndType(String customer, String type);
     *
     * }
     *
     * 这里，添加了两个方法。其中一个会查询 customer 属性等于给定 String 值的 Order 节点。另外一个方法
     * 与之类似，但是除了匹配 customer 属性以外，Order 节点的 type 属性必须还要等于给定的类型值。
     *
     * 之前已经讨论过查询方法的命名约定，所以这里没有必要再进行深入地讨论。
     *
     *
     *
     * 2、指定自定义查询
     *
     * 当命名约定无法满足需求时，还可以为方法添加 @Query 注解，为其指定自定义的查询。之前已经见过 @Query
     * 注解。在 Spring Data JPA 中，使用它来为 Repository 方法指定 JPA 查询。在 Spring Data MongoDB
     * 中，使用它来指定匹配 JSON 的查询。但是，在使用 Spring Data Neo4j 的时候，必须指定 Cypher 查询：
     *
     * @Query("match (o:Order)-[:HAS_ITEMS]->(i:Item) where i.product='Spring in Action' return o" )
     * List<Order> findSpringInActionOrders();
     *
     * 在这里，findSpringInActionOrders() 方法上使用了 @Query 注解，并设置了一个 Cypher 查询，它会
     * 查找与 Item 关联并且 product 属性等于 "Spring in Action" 的所有 Order 节点。
     *
     *
     *
     * 3、混合自定义的 Repository
     *
     * 当命名约定和 @Query 注解均无法满足满足需求的时候，还可以混合自定义的 Repository 逻辑。
     *
     * 例如，假设想自己编写 findSpringInActionOrders() 方法的实现，而不是依赖于 @Query 注解。那么可以
     * 首先定义一个中间接口，该接口包含 findSpringInActionOrders() 方法的定义：
     *
     * public interface OrderOperations {
     *
     *     List<Order> findSpringInActionOrders();
     *
     * }
     *
     * 然后，修改 OrderRepository，让它扩展 OrderOperations 和 GraphRepository：
     *
     * public interface OrderRepository extends GraphRepository<Order>, OrderOperations {
     *
     * ...
     *
     * }
     *
     * 最后，需要自己编写实现。与 Spring Data JPA 和 Spring Data MongoDB 类似，Spring Data Neo4j
     * 将会查找名字与 Repository 接口相同且添加 "Impl" 后缀的实现类。因此，需要创建 OrderRepositoryImpl
     * 类。如下代码展示了 OrderRepositoryImpl 类，它实现了 findSpringInActionOrders() 方法。
     *
     * public class OrderRepositoryImpl implements OrderOperations {
     *
     *     private final Neo4jOperations neo4j;
     *
     *     @Autowired
     *     public OrderRepositoryImpl(Neo4jOperations neo4j) {
     *         this.neo4j = neo4j;
     *     }
     *
     *     @Override
     *     public List<Order> findSpringInActionOrders() {
     *         Result<Map<String, Object>> result = neo4j.query(
     *                 "match (o:Order)-[:HAS_ITEMS]->(i:Item) " +
     *                         "where i.product='Spring in Action' return o", null);
     *         return IteratorUtil.asList(result.to(Order.class));
     *     }
     *
     * }
     *
     * OrderRepositoryImpl 中注入了一个 Neo4jOperations（具体来讲，就是 Neo4jTemplate 的实例），
     * 它会用来查询数据库。因为 query() 方法返回的是 Result<Map<String, Object>>，需要将其转换为
     * List<Order>。第一步是调用 Result 的 to() 方法，产生一个 EndResult<Order>。然后，使用 Neo4j
     * 的 IteratorUtil.asList() 方法将 EndResult<Order> 转换为 List<Order>，然后将其返回。
     *
     * 对于能够表达为节点和关联关系的数据，像 Neo4j 这样的图数据库是非常合适的。如果将生活的世界理解为
     * 各种互相关联的事物，那么图数据库能够适用于很大的范围。就个人而言，非常喜欢 Neo4j。
     *
     * 但有些时候，需要的数据会更简单一些。有时，所需要的仅仅将某个 value 存储起来，稍后能够根据一个 key
     * 将其提取出来。后续将会介绍 Spring Data 如何使用 Redis key-value 存储实现这种类型的数据持久化。
     */
    public static void main(String[] args) {

    }

}
