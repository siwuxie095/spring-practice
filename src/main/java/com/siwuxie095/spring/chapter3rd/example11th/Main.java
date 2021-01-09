package com.siwuxie095.spring.chapter3rd.example11th;

/**
 * @author Jiajing Li
 * @date 2021-01-09 16:35:47
 */
public class Main {

    /**
     * 小结
     *
     * 在这里，学习了一些强大的高级装配技巧。
     *
     * 首先，学习了 Spring profile，它解决了 Spring bean 要跨各种部署环境的通用问题。在运行时，通过将环境相关
     * 的 bean 与当前激活的 profile 进行匹配，Spring 能够让相同的部署单元跨多种环境运行，而不需要进行重新构建。
     *
     * Profile bean 是在运行时条件化创建 bean 的一种方式，但是 Spring 4 提供了一种更为通用的方式，通过这种方
     * 式能够声明某些 bean 的创建与否要依赖于给定条件的输出结果。结合使用 @Conditional 注解和 Spring Condition
     * 接口的实现，能够为开发人员提供一种强大和灵活的机制，实现条件化地创建 bean。
     *
     * 还看到了两种解决自动装配歧义性的方法：首选 bean 以及限定符。尽管将某个 bean 设置为首选 bean 是很简单的，
     * 但这种方式也有其局限性，所以讨论了如何将一组可选的自动装配 bean，借助限定符将其范围缩小到只有一个符合条件的
     * bean。除此之外，还看到了如何创建自定义的限定符注解，这些限定符描述了 bean 的特性。
     *
     * 尽管大多数的 Spring bean 都是以单例的方式创建的，但有的时候其他的创建策略更为合适。Spring 能够让 bean
     * 以单例、原型、请求作用域或会话作用域的方式来创建。在声明请求作用域或会话作用域的 bean 的时候，还学习了如何
     * 创建作用域代理，它分为基于类的代理和基于接口的代理的两种方式。
     *
     * 最后，学习了 Spring 表达式语言，它能够在运行时计算要注入到 bean 属性中的值。
     */
    public static void main(String[] args) {

    }

}
