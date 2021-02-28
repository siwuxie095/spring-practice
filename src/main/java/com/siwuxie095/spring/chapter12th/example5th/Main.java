package com.siwuxie095.spring.chapter12th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-02-28 19:36:22
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 MongoTemplate 访问 MongoDB
     *
     * 已经在配置类中配置了 MongoTemplate bean，不管是显式声明还是扩展 AbstractMongoConfiguration 都能实现相同的效果。
     * 接下来，需要做的就是将其注入到使用它的地方：
     *
     * @Autowired
     * MongoOperations mongo;
     *
     * 注意，在这里将 MongoTemplate 注入到一个类型为 MongoOperations 的属性中。MongoOperations 是 MongoTemplate 所
     * 实现的接口，不使用具体实现是一个好的做法，尤其是在注入的时候。
     *
     * MongoOperations 暴露了多个使用 MongoDB 文档数据库的方法。在这里，不可能讨论所有的方法，但是可以看一下最为常用的几
     * 个操作，比如计算文档集合中有多少条文档。使用注入的 MongoOperations，可以得到 Order 集合并调用 count() 来得到数量：
     *
     * long orderCount = mongo.getCollection("order").count();
     *
     * 现在，假设要保存一个新的 Order。为了完成这个任务，可以调用 save() 方法：
     *
     * Order order = new Order();
     * // ... set properties and add line items
     * mongo.save(order, "order");
     *
     * save() 方法的第一个参数是新创建的 Order，第二个参数是要保存的文档存储的名称。
     *
     * 另外，还可以调用 findById() 方法来根据 ID 查找订单：
     *
     * String orderId = "";
     * Order order = mongo.findById(orderId, Order.class);
     *
     * 对于更高级的查询，需要构造 Query 对象并将其传递给 find() 方法。例如，要查找所有 client 域等于 "Chuck Wagon" 的
     * 订单，可以使用如下的代码：
     *
     * List<Order> chucksOrders = mongo.find(Query.query(
     *      Criteria.where("client").is("Chuck Wagon")), Order.class);
     *
     * 在本例中，用来构造 Query 对象的 Criteria 只检查了一个域，但是它也可以用来构造更加有意思的查询。
     *
     * 比如，想要查询 Chuck 所有通过 Web 创建的订单：
     *
     * List<Order> chucksOrders = mongo.find(Query.query(
     *      Criteria.where("client").is("Chuck Wagon")
     *      .and("type").is("WEB")), Order.class);
     *
     * 如果你想移除某一个文档的话，那么就应该使用 remove() 方法：
     *
     * mongo.remove(order);
     *
     * MongoOperations 有多个操作文档数据的方法。这里建议你查看一下它的 JavaDoc 文档，以了解通过 MongoOperations 都能
     * 完成什么功能。
     *
     * 通常来讲，会将 MongoOperations 注入到自己设计的 Repository 类中，并使用它的操作来实现 Repository 方法。但是，如
     * 果你不愿意编写 Repository 的话，那么 Spring Data MongoDB 能够自动在运行时生成 Repository 实现。后续将会对其进行
     * 介绍。
     */
    public static void main(String[] args) {

    }

}
