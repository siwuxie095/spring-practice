package com.siwuxie095.spring.chapter12th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-02-28 19:15:35
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 为模型添加注解，实现 MongoDB 持久化
     *
     * 当使用 JPA 的时候，需要将 Java 实体类映射到关系型表和列上。JPA 规范提供了一些支持对象-关系映射的注解，
     * 而有一些 JPA 实现，如 Hibernate，也添加了自己的映射注解。
     *
     * 但是，MongoDB 并没有提供对象-文档映射的注解。Spring Data MongoDB 填补了这一空白，提供了一些将 Java
     * 类型映射为 MongoDB 文档的注解。如下描述了这些注解。
     * （1）@Document：标示映射到 MongoDB 文档上的领域对象
     * （2）@Id：标示某个域为 ID 域
     * （3）@DbRef：标示某个域要引用其他的文档，这个文档有可能位于另外一个数据库中
     * （4）@Field：为文档域指定自定义的元数据
     * （5）@Version：标示某个属性用作版本域
     *
     * @Document 和 @Id 注解类似于 JPA 的 @Entity 和 @Id 注解。会经常使用这两个注解，对于要以文档形式保存
     * 到 MongoDB 数据库的每个 Java 类型都会使用这两个注解。例如，如下代码展现了如何为 Order 类添加注解，它
     * 会被持久化到 MongoDB 中。
     *
     * @Document
     * public class Order {
     *
     *     @Id
     *     private String id;
     *
     *     @Field("customer")
     *     private String customer;
     *
     *     private String type;
     *
     *     private Collection<Item> items = new LinkedHashSet<Item>();
     *
     *     public String getCustomer() {
     *         return customer;
     *     }
     *
     *     public void setCustomer(String customer) {
     *         this.customer = customer;
     *     }
     *
     *     public String getType() {
     *         return type;
     *     }
     *
     *     public void setType(String type) {
     *         this.type = type;
     *     }
     *
     *     public Collection<Item> getItems() {
     *         return items;
     *     }
     *
     *     public void setItems(Collection<Item> items) {
     *         this.items = items;
     *     }
     *
     *     public String getId() {
     *         return id;
     *     }
     *
     * }
     *
     * 可以看到，Order 类添加了 @Document 注解，这样它就能够借助 MongoTemplate 或自动生成的 Repository
     * 进行持久化。其 id 属性上使用了 @Id 注解，用来指定它作为文档的 ID。除此之外，customer 属性上使用了
     * @Field 注解，这样的话，当文档持久化的时候 customer 属性将会映射为名为 client 的域。
     *
     * 注意，其他的属性并没有添加注解。除非将属性设置为瞬时态（transient）的，否则 Java 对象中所有的域都会持
     * 久化为文档中的域。并且如果不使用 @Field 注解进行设置，文档域中的名字将会与对应的 Java 属性相同。
     *
     * 同时，需要注意的是 items 属性，它指的是订单中具体条目的集合。在传统的关系型数据库中，这些条目将会保存在
     * 另外的一个数据库表中，通过外键进行应用，items 域上很可能还会使用 JPA 的 @OneToMany 注解。但在这里，
     * 情形完全不同。
     *
     * PS：文档展现了关联但非规范化的数据。相关的概念（如订单中的条目）被嵌入到顶层的文档数据中。
     *
     * 如前面所述，文档可以与其他的文档产生关联，但这并不是文档数据库所擅长的功能。在本例购买订单与行条目之间的
     * 关联关系中，行条目只是同一个订单文档里面内嵌的一部分。因此，没有必要为这种关联关系添加任何注解。实际上，
     * Item 类本身并没有任何注解。
     *
     * public class Item {
     *
     *     private Long id;
     *
     *     private Order order;
     *
     *     private String product;
     *
     *     private double price;
     *
     *     private int quantity;
     *
     *     public Order getOrder() {
     *         return order;
     *     }
     *
     *     public String getProduct() {
     *         return product;
     *     }
     *
     *     public void setProduct(String product) {
     *         this.product = product;
     *     }
     *
     *     public double getPrice() {
     *         return price;
     *     }
     *
     *     public void setPrice(double price) {
     *         this.price = price;
     *     }
     *
     *     public int getQuantity() {
     *         return quantity;
     *     }
     *
     *     public void setQuantity(int quantity) {
     *         this.quantity = quantity;
     *     }
     *
     *     public Long getId() {
     *         return id;
     *     }
     *
     * }
     *
     * 这里没有必要为 Item 添加 @Document 注解，也没有必要为它的域指定 @Id。这是因为不会单独将 Item 持久化
     * 为文档。它始终会是 Order 文档中 Item 列表的一个成员，并且会作为文档中的嵌入元素。
     *
     * 当然，如果你想指定 Item 中的某个域如何持久化到文档中，那么可以为对应的 Item 属性添加 @Field 注解。不
     * 过在这里，并没有必要这样做。
     *
     * 现在已经为 Java 对象添加了 MongoDB 持久化的注解。后续将会介绍如何使用 MongoTemplate 来存储它们。
     */
    public static void main(String[] args) {

    }

}
