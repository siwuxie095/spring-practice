package com.siwuxie095.spring.chapter4th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-01-15 07:55:52
 */
public class Main {

    /**
     * Spring 对 AOP 的支持
     *
     * 并不是所有的 AOP 框架都是相同的，它们在连接点模型上可能有强弱之分。有些允许在字段修饰符级别应用通知，而另一些只
     * 支持与方法调用相关的连接点。它们织入切面的方式和时机也有所不同。但是无论如何，创建切点来定义切面所织入的连接点是
     * AOP 框架的基本功能。
     *
     * 这里更多的会关注 Spring AOP。虽然如此，Spring 和 AspectJ 项目之间有大量的协作，而且 Spring 对 AOP 的支持
     * 在很多方面借鉴了 AspectJ 项目。
     *
     * Spring 提供了 4 种类型的 AOP 支持：
     * （1）基于代理的经典 Spring AOP；
     * （2）纯 POJO 切面；
     * （3）@AspectJ 注解驱动的切面；
     * （4）注入式 AspectJ 切面（适用于 Spring 各版本）。
     *
     * 前三种都是 Spring AOP 实现的变体，Spring AOP 构建在动态代理基础之上，因此，Spring 对 AOP 的支持局限于方法
     * 拦截。
     *
     * 术语 "经典" 通常意味着是很好的东西。老爷车、经典高尔夫球赛、可口可乐精品都是好东西。但是 Spring 的经典 AOP
     * 编程模型并不怎么样。当然，曾经它的确非常棒。但是现在 Spring 提供了更简洁和干净的面向切面编程方式。引入了简单的
     * 声明式 AOP 和基于注解的 AOP 之后，Spring 经典的 AOP 看起来就显得非常笨重和过于复杂，直接使用 ProxyFactory
     * Bean 会让人感觉厌烦。所以这里不会再介绍经典的 Spring AOP。
     *
     * 借助 Spring 的 aop 命名空间，可以将纯 POJO 转换为切面。实际上，这些 POJO 只是提供了满足切点条件时所要调用的
     * 方法。遗憾的是，这种技术需要 XML 配置，但这的确是声明式地将对象转换为切面的简便方式。
     *
     * Spring 借鉴了 AspectJ 的切面，以提供注解驱动的 AOP。本质上，它依然是 Spring 基于代理的 AOP，但是编程模型几
     * 乎与编写成熟的 AspectJ 注解切面完全一致。这种 AOP 风格的好处在于能够不使用 XML 来完成功能。
     *
     * 如果你的 AOP 需求超过了简单的方法调用（如构造器或属性拦截），那么你需要考虑使用 AspectJ 来实现切面。在这种情况
     * 下，上面所示的第四种类型能够帮助你将值注入到 AspectJ 驱动的切面中。
     *
     * 下面将介绍 Spring AOP 框架的一些关键知识。
     *
     *
     *
     * 1、Spring 通知是 Java 编写的
     *
     * Spring 所创建的通知都是用标准的 Java 类编写的。这样的话，就可以使用与普通 Java 开发一样的集成开发环境（IDE）
     * 来开发切面。而且，定义通知所应用的切点通常会使用注解或在 Spring 配置文件里采用 XML 来编写，这两种语法对于 Java
     * 开发者来说都是相当熟悉的。
     *
     * AspectJ 与之相反。虽然 AspectJ 现在支持基于注解的切面，但 AspectJ 最初是以 Java 语言扩展的方式实现的。这种
     * 方式有优点也有缺点。通过特有的 AOP 语言，可以获得更强大和细粒度的控制，以及更丰富的 AOP 工具集，但是需要额外学
     * 习新的工具和语法。
     *
     *
     *
     * 2、Spring 在运行时通知对象
     *
     * 通过在代理类中包裹切面，Spring 在运行期把切面织入到 Spring 管理的 bean 中。代理类封装了目标类，并拦截被通知
     * 方法的调用，再把调用转发给真正的目标 bean。当代理拦截到方法调用时，在调用目标 bean 方法之前，会执行切面逻辑。
     *
     * PS：Spring 的切面由包裹了目标对象的代理类实现。代理类处理方法的调用，执行额外的切面逻辑，并调用目标方法。
     *
     * 直到应用需要被代理的 bean 时，Spring 才创建代理对象。如果使用的是 ApplicationContext，在 ApplicationContext
     * 从 BeanFactory 中加载所有 bean 的时候，Spring 才会创建被代理的对象。因为 Spring 运行时才创建代理对象，所以
     * 不需要特殊的编译器来织入 Spring AOP 的切面。
     *
     *
     *
     * 3、Spring 只支持方法级别的连接点
     *
     * 通过使用各种 AOP 方案可以支持多种连接点模型。因为 Spring 基于动态代理，所以 Spring 只支持方法连接点。这与一些
     * 其他的 AOP 框架是不同的，例如 AspectJ 和 JBoss，除了方法切点，它们还提供了字段和构造器接入点。Spring 缺少对
     * 字段连接点的支持，无法创建细粒度的通知，例如拦截对象字段的修改。而且它不支持构造器连接点，这样就无法在 bean 创建
     * 时应用通知。
     *
     * 但是方法拦截可以满足绝大部分的需求。如果需要方法拦截之外的连接点拦截功能，那么可以利用 Aspect 来补充 Spring AOP
     * 的功能。
     */
    public static void main(String[] args) {

    }

}
