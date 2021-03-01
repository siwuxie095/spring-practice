package com.siwuxie095.spring.chapter12th.example9th;

/**
 * @author Jiajing Li
 * @date 2021-03-01 21:59:42
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用注解标注图实体
     *
     * Neo4j 定义了两种类型的实体：
     * （1）节点（node）
     * （2）关联关系（relationship）
     *
     * 一般来讲，节点反映了应用中的事物，而关联关系定义了这些事物是如何联系在一起的。
     *
     * Spring Data Neo4j 提供了多个注解，它们可以应用在模型类型及其域上，实现 Neo4j 中的持久化。这些注解如下所示。
     * （1）@NodeEntity：将 Java 类型声明为节点实体；
     * （2）@RelationshipEntity：将 Java 类型声明为关联关系实体；
     * （3）@StartNode：将某个属性声明为关联关系实体的开始节点；
     * （4）@EndNode：将某个属性声明为关联关系实体的结束节点；
     * （5）@Fetch：将实体的属性声明为立即加载；
     * （6）@GraphId：将某个属性设置为实体的 ID 域（这个域的类型必须是 Long）；
     * （7）@GraphProperty：明确声明某个属性；
     * （8）@GraphTraversal：声明某个属性会自动提供一个 iterable 元素，这个元素是图遍历所构建的；
     * （9）@Indexed：声明某个属性应该被索引；
     * （10）@Labels：为 @NodeEntity 声明标签；
     * （11）@Query：声明某个属性会自动提供一个 iterable 元素，这个元素是执行给定的 Cypher 查询所构建的；
     * （12）@QueryResult：声明某个 Java 或接口能够持有查询的结果；
     * （13）@RelatedTo：通过某个属性，声明当前的 @NodeEntity 与另外一个 @NodeEntity 之间的关联关系；
     * （14）@RelatedToVia：在 @NodeEntity 上声明某个属性，指定其引用该节点所属的某一个 @RelationshipEntity；
     * （15）@RelationshipType：将某个域声明为关联实体类型；
     * （16）@ResultColumn：在带有 @QueryResult 注解的类型上，将某个属性声明为获取查询结果集中的某个特定列。
     *
     * PS：借助 Spring Data Neo4j 的注解，能够将领域类型映射为图中的节点和关联关系。
     *
     * 为了了解如何使用其中的某些注解，会将其应用到订单/条目样例中。
     *
     * 在该样例中，数据建模的一种方式就是将订单设定为一个节点，它会与一个或多个条目关联。
     *
     * 为了将订单指定为节点，需要为 Order 类添加 @NodeEntity 注解。如下代码展现了带有 @NodeEntity 注解的 Order
     * 类，它还包含了几个其他注解。
     *
     * @NodeEntity
     * public class Order {
     *
     *     @GraphId
     *     private Long id;
     *
     *     private String customer;
     *
     *     private String type;
     *
     *     @RelatedTo(type = "HAS_ITEMS")
     *     private Set<Item> items = new LinkedHashSet<Item>();
     *
     *     ...
     *
     * }
     *
     * 除了类级别上的 @NodeEntity，还要注意 id 属性上使用了 @GraphId 注解。Neo4j 上的所有实体必要要有一个图 ID。
     * 这大致类似于 JPA @Entity 以及 MongoDB @Document 类中使用 @Id 注解的属性。在这里，@GraphId 注解标注的属
     * 性必须是 Long 类型。
     *
     * customer 和 type 属性上没有任何注解。只要这些属性不是瞬态的，它们都会成为数据库中节点的属性。
     *
     * items 属性上使用了 @RelatedTo 注解，这表明 Order 与一个 Item 的 Set 存在关联关系。type 属性实际上就是为
     * 关联关系建立了一个文本标记。它可以设置成任意的值，但通常会给定一个易于人类阅读的文本，用来简单描述这个关联关系
     * 的特征。后续你将会看到如何将这个标记用在查询中，实现跨关联关系的查询。
     *
     * 就 Item 本身来说，下面展现了如何为其添加注解实现图的持久化。
     *
     * @NodeEntity
     * public class Item {
     *
     *     @GraphId
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
     *     ...
     *
     * }
     *
     * 类似于 Order，Item 也使用了 @NodeEntity 注解，将其标记为一个节点。它同时也有一个 Long 类型的属性，借助
     * @GraphId 注解将其标注为节点的图 ID，而 product、price 以及 quantity 属性均会作为图数据库中节点的属性。
     *
     * Order 和 Item 之间的关联关系很简单，关系本身并不包含任何的数据。因此，@RelatedTo 注解就足以定义关联关系。
     * 但是，并不是所有的关联关系都这么简单。
     *
     * 现在重新考虑该如何为数据建模，从而学习如何使用更为复杂的关联关系。在当前的数据模型中，将条目和产品的信息组合
     * 到了 Item 类中。但是，当重新考虑的时候，会发现订单会与一个或多个产品相关联。订单与产品之间的关系构成了订单
     * 的一个条目。
     *
     * 在这个新的模型中，订单中产品的数量是条目中的一个属性，而产品本身是另外一个概念。与前面一样，订单和产品都是节点，
     * 而条目是关联关系。因为现在的条目必须要包含一个数量值，关联关系不像前面那么简单。所以需要定义一个类来代表条目，
     * 比如如下代码所示的 LineItem。
     *
     * @RelationshipEntity(type = "HAS_LINE_ITEM_FOR")
     * public class LineItem {
     *
     *     @GraphId
     *     private Long id;
     *
     *     @StartNode
     *     private Order order;
     *
     *     @EndNode
     *     private Product product;
     *
     *     private int quantity;
     *
     *     ...
     *
     * }
     *
     * Order 类通过 @NodeEntity 注解将其标示为一个节点，而 LineItem 类则使用了 @RelationshipEntity 注解。
     * LineItem 同样也有一个 id 属性标注了 @GraphId 注解，不管是节点实体还是关联关系实体，都必须要有一个图 ID，
     * 而且其类型必须为 Long。
     *
     * 关联关系实体的特殊之处在于它们连接了两个节点。@StartNode 和 @EndNode 注解用在定义关联关系两端的属性上。
     * 在本例中，Order 是开始节点，Product 是结束节点。
     *
     * 最后，LineItem 类有一个 quantity 属性，当关联关系创建的时候，它会持久化到数据库中。
     *
     * 领域对象已经添加了注解，现在就可以保存与读取节点和关联关系了。后续将会介绍如何使用 Spring Data Neo4j 中
     * 的 Neo4jTemplate 实现面向模板的数据访问。
     */
    public static void main(String[] args) {

    }

}
