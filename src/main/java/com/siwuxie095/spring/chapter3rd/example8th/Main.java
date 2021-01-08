package com.siwuxie095.spring.chapter3rd.example8th;

/**
 * @author Jiajing Li
 * @date 2021-01-08 21:17:02
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 运行时值注入
     *
     * 当讨论依赖注入的时候，通常所讨论的是将一个 bean 引用注入到另一个 bean 的属性或构造器参数中。它通常来讲指
     * 的是将一个对象与另一个对象进行关联。
     *
     * 但是 bean 装配的另外一个方面指的是将一个值注入到 bean 的属性或者构造器参数中。
     *
     * 如将专辑的名字装配到 BlankDisc bean 的构造器或 title 属性中。例如，可能按照这样的方式来组装 BlankDisc：
     *
     *     @Bean
     *     public CompactDisc compactDisc() {
     *         return new BlankDisc("Sgt. Pepper's Lonely Hearts Club Band", "The Beatles");
     *     }
     *
     * 尽管这实现了你的需求，也就是为 BlankDisc bean 设置 title 和 artist，但它在实现的时候是将值硬编码在配置
     * 类中的。与之类似，如果使用 XML 的话，那么值也会是硬编码的：
     *
     *     <bean id="compactDisc"
     *           class="com.siwuxie095.spring.chapter2nd.example6th.BlankDisc"
     *           c:_0="Sgt. Pepper's Lonely Hearts Club Band"
     *           c:_1="The Beatles">
     *     </bean>
     *
     * 有时候硬编码是可以的，但有的时候，可能会希望避免硬编码值，而是想让这些值在运行时再确定。为了实现这些功能，
     * Spring 提供了两种在运行时求值的方式：
     * （1）属性占位符（Property placeholder）
     * （2）Spring 表达式语言（SpEL）
     *
     * 这两种技术的用法是类似的，不过它们的目的和行为是有所差别的。属性占位符较为简单，SpEL 则更为强大。
     */
    public static void main(String[] args) {

    }

}
