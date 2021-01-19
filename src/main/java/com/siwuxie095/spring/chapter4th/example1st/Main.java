package com.siwuxie095.spring.chapter4th.example1st;

/**
 * @author Jiajing Li
 * @date 2021-01-12 08:10:04
 */
public class Main {

    /**
     * 面向切面的 Spring
     *
     * 每家每户都有一个电表来记录用电量，每个月都会有人来查电表，这样电力公司就知道应该收取多少费用了。
     *
     * 现在想象一下，如果没有电表，也没有人来查看用电量，假设现在由户主来联系电力公司并报告自己的用电量。虽然可能会有一些特别
     * 执着的户主会详细记录使用电灯、电视和空调的情况，但大多数人肯定不会这么做。基于信用的电力收费对于消费者可能非常不错，但
     * 对于电力公司来说结果可能就不那么美妙了。
     *
     * 监控用电量是一个很重要的功能，但并不是大多数家庭重点关注的问题。所有家庭实际上所关注的可能是修剪草坪、用吸尘器清理地毯、
     * 打扫浴室等事项。从家庭的角度来看，监控房屋的用电量是一个被动事件。
     *
     * 软件系统中的一些功能就像家里的电表一样。这些功能需要用到应用程序的多个地方，但是又不想在每个点都明确调用它们。日志、安
     * 全和事务管理的确都很重要，但它们是否为应用对象主动参与的行为呢？如果让应用对象只关注于自己所针对的业务领域问题，而其他
     * 方面的问题由其他应用对象来处理，这会不会更好呢？
     *
     * 在软件开发中，散布于应用中多处的功能被称为横切关注点（cross-cutting concern）。通常来讲，这些横切关注点从概念上是与
     * 应用的业务逻辑相分离的（但是往往会直接嵌入到应用的业务逻辑之中）。把这些横切关注点与业务逻辑相分离正是面向切面编程（AOP）
     * 所要解决的问题。
     *
     * 已经知道，使用依赖注入（DI）可以管理和配置应用对象。DI 有助于应用对象之间的解耦，而 AOP 可以实现横切关注点与它们所影
     * 响的对象之间的解耦。
     *
     * 日志是应用切面的常见范例，但它并不是切面适用的唯一场景。切面所适用的场景还包括声明式事务、安全和缓存等。
     *
     * 这里将展示 Spring 对切面的支持，包括如何把普通类声明为一个切面和如何使用注解创建切面。除此之外，还会看到 AspectJ ——
     * 另一种流行的 AOP 实现 —— 如何补充 Spring AOP 框架的功能。
     *
     * 这里先不管事务、安全和缓存，不妨先看一下 Spring 是如何实现切面的，就从 AOP 的基础知识开始吧。
     */
    public static void main(String[] args) {

    }

}