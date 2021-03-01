package com.siwuxie095.spring.chapter12th.example10th;

/**
 * @author Jiajing Li
 * @date 2021-03-01 22:33:13
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Neo4jTemplate
     *
     * Spring Data MongoDB 提供了 MongoTemplate 实现基于模板的 MongoDB 持久化，与之类似，Spring Data Neo4j
     * 提供了 Neo4jTemplate 来操作 Neo4j 图数据库中的节点和关联关系。如果你已经按照前面的方式配置了 Spring Data
     * Neo4j，在 Spring 应用上下文中就已经具备了一个 Neo4jTemplate bean。接下来需要做的就是将其注入到任意想使用
     * 它的地方。
     *
     * 例如，可以直接将其自动装配到某个 bean 的属性上：
     *
     * @Autowired
     * private Neo4jOperations neo4j;
     *
     * Neo4jTemplate 定义了很多的方法，包括保存节点、删除节点以及创建节点间的关联关系。这里不会介绍所有的方法，但是
     * 会看一下 Neo4jTemplate 所提供的最为常用的方法。
     *
     * 这里想借助 Neo4jTemplate 完成的最基本的一件事情可能就是将某个对象保存为节点。假设这个对象已经使用了 @NodeEntity
     * 注解，那么可以按照如下的方式来使用 save() 方法：
     *
     * Order order = new Order();
     * Order savedOrder = neo4j.save(order);
     *
     * 如果你能知道对象的图 ID，那么可以通过 findOne() 方法来获取它：
     *
     * Order order = neo4j.findOne(42, Order.class);
     *
     * 如果按照给定的 ID 找不到节点的话，那么 findOne() 方法将会抛出 NotFound(Exception)。
     *
     * 如果你想获取给定类型的所有对象，那么可以使用 findAll() 方法：
     *
     * EndResult<Order> allOrders = neo4j.findAll(Order.class);
     *
     * 这里返回的 EndResult 是一个 Iterable，它能够用在 for-each 循环以及任何可以使用 Iterable 的地方。如果不
     * 存在这样的节点的话，findAll() 方法将会返回空的 Iterable。
     *
     * 如果你只是想知道 Neo4j 数据库中指定类型的对象数量，那么就可以调用 count() 方法：
     *
     * long orderCount = neo4j.count(Order.class);
     *
     * delete() 方法可以用来删除对象：
     *
     * neo4j.delete(order);
     *
     * createRelationshipBetween() 是 Neo4jTemplate 所提供的最有意思的方法之一。可以猜到，它会为两个节点创建
     * 关联关系。例如，可以在 Order 节点和 Product 节点之间建立 LineItem 关联关系：
     *
     * Order order = new Order();
     * Product prod = new Product();
     * LineItem lineItem = neo4j.createRelationshipBetween(
     *      order, prod, LineItem.class, "HAS_LINE_ITEM_FOR", false);
     * lineItem.setQuantity(5);
     * neo4j.save(lineItem);
     *
     * 该方法的前两个参数是关联关系两端的节点对象。接下来的参数指定了使用 @RelationshipEntity 注解的类型，它会代
     * 表这种关系。接下来的 String 值描述了关联关系的特征。最后的参数是一个 boolean 值，它表明这两个节点实体之间
     * 是否允许存在重复的关联关系。
     *
     * 同时该方法会返回关联关系类的一个实例。通过它，可以设置任意的属性。上面的示例中设置了 quantity 属性。当这一切
     * 完成后，调用 save() 方法将关联关系保存到数据库中。
     *
     * Neo4jTemplate 提供了很便利的方式来使用 Neo4j 图数据库中的节点和关联关系。但是，这种方式需要借助 Neo4jTemplate
     * 编写自己的 Repository 实现。后续将会介绍 Spring Data Neo4j 怎样自动化生成 Repository 实现。
     */
    public static void main(String[] args) {

    }

}
