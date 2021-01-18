package com.siwuxie095.spring.chapter4th.example9th;

/**
 * @author Jiajing Li
 * @date 2021-01-18 22:30:43
 */
public class Main {

    /**
     * 小结
     *
     * AOP 是面向对象编程的一个强大补充。通过 AspectJ，现在可以把之前分散在应用各处的行为放入可重用的模块中，并可以
     * 显式地声明在何处如何应用该行为。这有效减少了代码冗余，并让代码中的类关注自身的主要功能。
     *
     * Spring 提供了一个 AOP 框架，使得可以把切面插入到方法执行的周围。现在已经学会如何把通知织入前置、后置和环绕方
     * 法的调用中，以及为处理异常增加自定义的行为。
     *
     * 关于在 Spring 应用中如何使用切面，可以有多种选择。通过使用 @AspectJ 注解和简化的配置命名空间，在 Spring 中
     * 装配通知和切点变得非常简单。
     *
     * 最后，当 Spring AOP 不能满足需求时，必须转向更为强大的 AspectJ。对于这些场景，学习和了解了如何使用 Spring
     * 为 AspectJ 切面注入依赖。
     */
    public static void main(String[] args) {

    }

}
