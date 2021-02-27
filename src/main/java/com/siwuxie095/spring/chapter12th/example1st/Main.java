package com.siwuxie095.spring.chapter12th.example1st;

/**
 * @author Jiajing Li
 * @date 2021-02-27 08:42:43
 */
public class Main {

    /**
     * 使用 NoSQL 数据库
     *
     * 亨利 · 福特在他的自传中曾经写过一句很著名的话："任何顾客可以将这辆车漆成任何他所愿意的颜色，只要保持它的黑色就可以"。
     * 有人说这句话是傲慢和固执的，而有些人则说这句话反映出了他的幽默。事实上，在当时，他通过使用一种快速烘干的油漆降低了成
     * 本，而当时这种油漆只有黑色的。
     *
     * 福特的这句著名的话也可以用在数据库领域，多年来，开发者一直被告知，可以使用任意想要的数据库，只要它是关系型数据库就行。
     * 关系型数据库已经垄断应用开发领域好多年了。
     *
     * 随着一些竞争者进入数据库领域，关系型数据库的垄断地位开始被弱化。所谓的 "NoSQL" 数据库开始侵入生产型的应用之中，同时
     * 也认识到并没有一种全能型的数据库。现在有了更多的可选方案，所以能够为要解决的问题选择最佳的数据库。
     *
     * 在之前，主要关注于关系型数据库，首先使用 Spring 对 JDBC 支持，然后使用对象-关系映射。另外，还看到了 Spring Data
     * JPA，它是 Spring Data 项目下的多个子项目之一。通过在运行时自动生成 Repository 实现，Spring Data JPA 能够让使
     * 用 JPA 的过程更加简单容易。
     *
     * Spring Data 还提供了对多种 NoSQL 数据库的支持，包括 MongoDB、Neo4j 和 Redis。它不仅支持自动化的 Repository，
     * 还支持基于模板的数据访问和映射注解。在这里，将会看到如何为非关系型的 NoSQL 数据库编写 Repository。
     *
     * 首先，将从 Spring Data MongoDB 开始，看一下如何编写 Repository 来处理基于文档的数据。
     */
    public static void main(String[] args) {

    }

}
