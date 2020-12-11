package com.siwuxie095.spring.chapter1st.example3rd;

/**
 * @author Jiajing Li
 * @date 2020-12-11 08:13:05
 */
public class Main {

    /**
     * 激发 POJO 的潜能
     *
     * 如果你从事 Java 编程有一段时间了，那么你或许会发现（可能你也实际使用过）很多框架通过强迫应用继承它们的类
     * 或实现它们的接口从而导致应用与框架绑死。一个典型的例子是 EJB 2 时代的无状态会话 bean。早期的 EJB 是一
     * 个很容易想到的例子，不过这种侵入式的编程方式在早期版本的Struts、WebWork、Tapestry 以及无数其他的 Java
     * 规范和框架中都能看到。
     *
     * Spring 竭力避免因自身的 API 而弄乱你的应用代码。Spring 不会强迫你实现 Spring 规范的接口或继承 Spring
     * 规范的类，相反，在基于 Spring 构建的应用中，它的类通常没有任何痕迹表明你使用了 Spring。最坏的场景是，一
     * 个类或许会使用 Spring 注解，但它依旧是 POJO。
     *
     * 以 HelloWorldBean 为例。可以看到，这是一个简单普通的 Java 类 —— POJO。没有任何地方表明它是一个
     * Spring 组件。Spring 的非侵入编程模型意味着这个类在 Spring 应用和非 Spring 应用中都可以发挥同样
     * 的作用。
     *
     * 尽管形式看起来很简单，但 POJO 一样可以具有魔力。Spring 赋予 POJO 魔力的方式之一就是通过 DI 来装配它们。
     * 后续就来看看 DI 是如何帮助应用对象彼此之间保持松散耦合的。
     */
    public static void main(String[] args) {

    }

}
