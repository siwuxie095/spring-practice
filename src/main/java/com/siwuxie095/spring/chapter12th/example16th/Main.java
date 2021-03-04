package com.siwuxie095.spring.chapter12th.example16th;

/**
 * @author Jiajing Li
 * @date 2021-03-04 22:33:45
 */
public class Main {

    /**
     * 小结
     *
     * 关系型数据库作为数据持久化领域唯一可选方案的时代已经一去不返了。现在，有多种不同的数据库，每一种都代表了
     * 不同形式的数据，并提供了适应多种领域模型的功能。Spring Data 能够让开发者在 Spring 应用中使用这些数据
     * 库，并且使用一致的抽象方式访问各种数据库方案。
     *
     * 在这里，基于之前使用 JPA 时所学到的 Spring Data 知识，将其应用到了 MongoDB 文档数据库和 Neo4j 图数
     * 据库中。与 JPA 对应的功能类似，Spring Data MongoDB 和 Spring Data Neo4j 项目都提供了基于接口定义
     * 自动生成 Repository 的功能。除此之外，还看到了如何使用 Spring Data 所提供的注解将领域模型映射为文档、
     * 节点和关联关系。
     *
     * Spring Data 还支持将数据持久化到 Redis key-value 存储中。Key-value 存储明显要简单一些，因此没有必
     * 要支持自动化 Repository 和映射注解。不过，Spring Data Redis 还是提供了两个不同的模板类来使用 Redis
     * key-value 存储。
     *
     * 不管你选择使用哪种数据库，从数据库中获取数据都是消耗成本的操作。实际上，数据库查询是很多应用最大的性能瓶
     * 颈。已经看过了如何通过各种数据源存储和获取数据，现在看一下如何避免出现这种瓶颈。后续将会看到如何借助声明
     * 式缓存避免不必要的数据库查询。
     */
    public static void main(String[] args) {

    }

}
