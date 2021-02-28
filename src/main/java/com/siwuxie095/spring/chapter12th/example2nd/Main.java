package com.siwuxie095.spring.chapter12th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-02-28 18:05:48
 */
public class Main {

    /**
     * 使用 MongoDB 持久化文档数据
     *
     * 有一些数据的最佳表现形式是文档（document）。也就是说，不要把这些数据分散到多个表、节点或者实体中，将这些信息
     * 收集到一个非规范化（也就是文档）的结构中会更有意义。尽管两个或两个以上的文档有可能会彼此产生关联，但是通常来讲，
     * 文档是独立的实体。能够按照这种方式优化并处理文档的数据库，称之为文档数据库。
     *
     * 例如，假设要编写一个应用程序来获取大学生的成绩单，可能需要根据学生的名字来查询其成绩单，或者根据一些通用的属性
     * 来查询成绩单。但是，每个学生是相互独立的，任意的两个成绩单之间没有必要相互关联。尽管能够使用关系型数据库模式来
     * 获取成绩单数据（也许你曾经这样做过），但文档型数据库可能才是更好的方案。
     *
     *
     * 文档数据库不适用于什么场景
     *
     * 了解文档型数据库能够用于什么场景是很重要的。但是，知道文档型数据库在什么情况下不适用同样也是很重要的。文档数据
     * 库不是通用的数据库，它们所擅长解决的是一个很小的问题集。
     *
     * 有些数据具有明显的关联关系，文档型数据库并没有针对存储这样的数据进行优化。例如，社交网络表现了应用中不同的用户
     * 之间是如何建立关联的，这种情况就不适合放到文档型数据库中。在文档数据库中存储具有丰富关联关系的数据也并非完全不
     * 可能，但这样做的话，你通常会发现遇到的挑战要多于所带来的收益。
     *
     * Spittr 应用的域对象并不适合文档数据库。在这里，将会在一个购物订单系统中学习 MongoDB。
     *
     * MongoDB 是最为流行的开源文档数据库之一。Spring Data MongoDB 提供了三种方式在 Spring 应用中使用 MongoDB：
     * （1）通过注解实现对象-文档映射；
     * （2）使用 MongoTemplate 实现基于模板的数据库访问；
     * （3）自动化的运行时 Repository 生成功能。
     *
     * 已经看到 Spring Data JPA 如何为基于 JPA 的数据访问实现自动化 Repository 生成功能。Spring Data MongoDB
     * 为基于 MongoDB 的数据访问提供了相同的功能。
     *
     * 不过，与 Spring Data JPA 不同的是，Spring Data MongoDB 提供了将 Java 对象映射为文档的功能（PS：Spring
     * Data JPA 没有必要为 JPA 提供这样的注解，因为 JPA 规范本身就提供了对象-关系映射注解）。除此之外，Spring Data
     * MongoDB 为通用的文档操作任务提供了基于模板的数据访问方式。
     */
    public static void main(String[] args) {

    }

}
