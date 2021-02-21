package com.siwuxie095.spring.chapter10th.example1st;

/**
 * @author Jiajing Li
 * @date 2021-02-21 16:21:32
 */
public class Main {

    /**
     * 通过 Spring 和 JDBC 征服数据库
     *
     * 在掌握了 Spring 容器的核心知识之后，是时候将它在实际应用中进行使用了。数据持久化是一个非常不错的起点，
     * 因为几乎所有的企业级应用程序中都存在这样的需求。你可能处理过数据库访问功能，在实际的工作中可能会发现数
     * 据访问有一些不足之处。必须初始化数据访问框架、打开连接、处理各种异常和关闭连接。如果上述操作出现任何问
     * 题，都有可能损坏或删除珍贵的企业数据。如果你还未曾经历过因未妥善处理数据访问而带来的严重后果，那这里要
     * 提醒你这绝对不是什么好事情。
     *
     * 做事要追求尽善尽美，所以这里选择了 Spring。Spring 自带了一组数据访问框架，集成了多种数据访问技术。不
     * 管你是直接通过 JDBC 还是像 Hibernate 这样的对象关系映射（object-relational mapping，ORM）框架实
     * 现数据持久化，Spring 都能够帮你消除持久化代码中那些单调枯燥的数据访问逻辑。可以依赖 Spring 来处理底
     * 层的数据访问，这样就可以专注于应用程序中数据的管理了。
     *
     * 当开发 Spittr 应用的持久层的时候，会面临多种选择，可以使用 JDBC、Hibernate、Java 持久化 API（Java
     * Persistence API，JPA）或者其他任意的持久化框架。你可能还会考虑使用最近很流行的 NoSQL 数据库。
     *
     * PS：NoSQL 数据库，也被称为无模式数据库。
     *
     * 幸好，不管你选择哪种持久化方式，Spring 都能够提供支持。在这里，主要关注于 Spring 对 JDBC 的支持。
     */
    public static void main(String[] args) {

    }

}
