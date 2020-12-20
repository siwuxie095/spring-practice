package com.siwuxie095.spring.chapter1st.example12th;

/**
 * @author Jiajing Li
 * @date 2020-12-20 13:29:01
 */
public class Main {

    /**
     * 小结
     *
     * 现在，你应该对 Spring 的功能特性有了一个清晰的认识。Spring 致力于简化企业级 Java 开发，促进代码
     * 的松散耦合。成功的关键在于依赖注入和 AOP。
     *
     * 这里先体验了 Spring 的 DI。DI 是组装应用对象的一种方式，借助这种方式对象无需知道依赖来自何处或者
     * 依赖的实现方式。不同于自己获取依赖对象，对象会在运行期赋予它们所依赖的对象。依赖对象通常会通过接口
     * 了解所注入的对象，这样的话就能确保低耦合。
     *
     * 除了 DI，还简单介绍了 Spring 对 AOP 的支持。AOP 可以帮助应用将散落在各处的逻辑汇集于一处 —— 切
     * 面。当 Spring 装配 bean 的时候，这些切面能够在运行期编织起来，这样就能非常有效地赋予 bean 新的
     * 行为。
     *
     * 依赖注入和 AOP 是 Spring 框架最核心的部分，因此只有理解了如何应用 Spring 最关键的功能，你才有能
     * 力使用 Spring 框架的其他功能。这里只是触及了 Spring DI 和 AOP 特性的皮毛。后续将深入探讨 DI 和
     * AOP。
     */
    public static void main(String[] args) {

    }

}
