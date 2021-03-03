package com.siwuxie095.spring.chapter12th.example12th;

/**
 * @author Jiajing Li
 * @date 2021-03-03 08:31:54
 */
public class Main {

    /**
     * 使用 Redis 操作 key-value 数据
     *
     * Redis 是一种特殊类型的数据库，它被称之为 key-value 存储。顾名思义，key-value 存储保存的是键值对。
     * 实际上，key-value 存储与哈希 Map 有很大的相似性。可以不太夸张地说，它们就是持久化的哈希 Map。
     *
     * 当你思考这一点的时候，可能会意识到，对于哈希 Map 或者 key-value 存储来说，其实并没有太多的操作。
     * 开发者可以将某个 value 存储到特定的 key 上，并且能够根据特定 key，获取 value。差不多也就是这样了。
     * 因此，Spring Data 的自动 Repository 生成功能并没有应用到 Redis 上。不过，Spring Data 的另外
     * 一个关键特性，也就是面向模板的数据访问，能够在使用 Redis 的时候，提供帮助。
     *
     * Spring Data Redis 包含了多个模板实现，用来完成 Redis 数据库的数据存取功能。后续将会介绍如何使用
     * 它们。但是为了创建 Spring Data Redis 的模板，首先需要有一个 Redis 连接工厂。幸好，Spring Data
     * Redis 提供了四个连接工厂以供选择。
     */
    public static void main(String[] args) {

    }

}
