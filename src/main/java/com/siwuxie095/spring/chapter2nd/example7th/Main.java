package com.siwuxie095.spring.chapter2nd.example7th;

/**
 * @author Jiajing Li
 * @date 2020-12-25 22:17:13
 */
public class Main {

    /**
     * 小结
     *
     * Spring 框架的核心是 Spring 容器。容器负责管理应用中组件的生命周期，它会创建这些组件并保证它们的依赖
     * 能够得到满足，这样的话，组件才能完成预定的任务。
     *
     * 在这里，看到了在 Spring 中装配 bean 的三种主要方式：
     * （1）自动化配置；
     * （2）基于 Java 的显式配置；
     * （3）基于 XML 的显式配置。
     *
     * 不管你采用什么方式，这些技术都描述了 Spring 应用中的组件以及这些组件之间的关系。
     *
     * 同时建议尽可能使用自动化配置，以避免显式配置所带来的维护成本。但是，如果你确实需要显式配置 Spring 的
     * 话，应该优先选择基于 Java 的配置，它比基于 XML 的配置更加强大、类型安全并且易于重构。
     */
    public static void main(String[] args) {

    }

}
