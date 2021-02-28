package com.siwuxie095.spring.chapter12th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-02-28 22:31:07
 */
public class Main {

    /**
     * 使用 Neo4j 操作图数据
     *
     * 文档型数据库会将数据存储到粗粒度的文档中，而图数据库会将数据存储到多个细粒度的节点中，这些节点之间通过
     * 关系建立关联。图数据库中的一个节点通常会对应数据库中的一个概念（concept），它会具备描述节点状态的属性。
     * 连接两个节点的关联关系可能也会带有属性。
     *
     * 按照其最简单的形式，图数据库比文档数据库更加通用，有可能会成为关系型数据库的无模式（schemaless）替代
     * 方案。因为数据的结构是图，所以可以遍历关联关系以查找数据中你所关心的内容，这在其他数据库中是很难甚至是
     * 无法实现的。
     *
     * Spring Data Neo4j 提供了很多与 Spring Data JPA 和 Spring Data MongoDB 相同的功能，当然所针对
     * 的是 Neo4j 图数据库。它提供了将 Java 对象映射到节点和关联关系的注解、面向模板的 Neo4j 访问方式以及
     * Repository 实现的自动化生成功能。
     *
     * 后续会看到如何在 Neo4j 中使用这些特性，不过首先需要配置 Spring Data Neo4j。
     */
    public static void main(String[] args) {

    }

}
