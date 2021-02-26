package com.siwuxie095.spring.chapter11th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-02-26 22:24:23
 */
public class Main {

    /**
     * 小结
     *
     * 对于很多应用来讲，关系型数据库是主流的数据存储形式，并且这种情况已经持续了很多年。使用 JDBC 并且将对象
     * 映射为数据库表是很烦琐乏味的事情，像 Hibernate 和 JPA 这样的 ORM 方案能够以更加声明式的模型实现数据
     * 持久化。尽管 Spring 没有为 ORM 提供直接的支持，但是它能够与多种流行的 ORM 方案集成，包括 Hibernate
     * 与 Java 持久化 API。
     *
     * 在这里，看到了如何在 Spring 应用中使用 Hibernate 的上下文 Session，这样的 Repository 就能包含很少
     * 甚至不包含 Spring 相关的代码。与之类似，还看到了如何将 EntityManagerFactory 或 EntityManager 注
     * 入到 Repository 实现中，从而实现不依赖于 Spring 的 JPA Repository。
     *
     * 稍后初步了解了 Spring Data，在这个过程中，只需声明 JPA Repository 接口即可，让 Spring Data JPA
     * 在运行时自动生成这些接口的实现。当需要的 Repository 方法超出了 Spring Data JPA 所提供的功能时，可
     * 以借助 @Query 注解以及编写自定义的 Repository 方法来实现。
     *
     * 但是，对于 Spring Data 的整体功能来说，这里只是接触到了皮毛。后续将会更加深入地学习 Spring Data 的
     * 方法命名 DSL，以及 Spring Data 如何为关系型数据库以外的领域带来帮助。也就是说：将会看到 Spring Data
     * 如何支持新兴的 NoSQL 数据库，这些数据库在最近几年变得越来越流行。
     */
    public static void main(String[] args) {

    }

}
