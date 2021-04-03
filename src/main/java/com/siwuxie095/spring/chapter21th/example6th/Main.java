package com.siwuxie095.spring.chapter21th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-04-03 14:52:14
 */
public class Main {

    /**
     * 小结
     *
     * Spring Boot 是 Spring 家族中一个令人兴奋的新项目。Spring 致力于简化 Java 开发，而
     * Spring Boot 致力于让 Spring 本身更加简单。
     *
     * Spring Boot 用了两个技巧来消除 Spring 项目中的样板式配置：Spring Boot Starter 和
     * 自动配置。
     *
     * 一个简单的 Spring Boot Starter 依赖能够替换掉 Maven 或 Gradle 构建中多个通用的依
     * 赖。例如，在项目中添加 Spring Boot Web 依赖后，将会引入 Spring Web 和 Spring MVC
     * 模块，以及 Jackson 2 数据绑定模块。
     *
     * 自动配置充分利用了 Spring 4.0 的条件化配置特性，能够自动配置特定的 Spring bean，用
     * 来启用某项特性。例如，Spring Boot 能够在应用的类路径中探测到 Thymeleaf，然后自动将
     * Thymeleaf 模板配置为 Spring MVC 视图的 bean。
     *
     * Spring Boot 的命令行接口（command-line interface，CLI）使用 Groovy 进一步简化了
     * Spring 项目。通过在 Groovy 代码中简单地引用 Spring 组件，CLI 就能自动添加所需的
     * Starter 依赖（而这又会触发自动配置）。除此之外，通过 Spring Boot CLI 运行时，很多的
     * Spring 类型都不需要在 Groovy 代码中显式使用 import 语句导入。
     *
     * 最后，Spring Boot Actuator 为基于 Spring Boot 开发的 Web 应用提供了一些通用的管
     * 理特性，包括查看线程 dump、Web 请求历史以及 Spring 应用上下文中的 bean。
     */
    public static void main(String[] args) {

    }

}
