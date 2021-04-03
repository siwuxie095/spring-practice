package com.siwuxie095.spring.chapter21th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-04-03 14:27:09
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 通过 Actuator 获取了解应用内部状况
     *
     * Spring Boot Actuator 所完成的主要功能就是为基于 Spring Boot 的应用添加多个有用的管理端点。这些端点
     * 包括以下几个内容：
     * （1）GET /autoconfig：描述了 Spring Boot 在使用自动配置的时候，所做出的决策；
     * （2）GET /beans：列出运行应用所配置的 bean；
     * （3）GET /configprops：列出应用中能够用来配置 bean 的所有属性及其当前的值；
     * （4）GET /dump：列出应用的线程，包括每个线程的栈跟踪信息；
     * （5）GET /env：列出应用上下文中所有可用的环境和系统属性变量；
     * （6）GET /env/{name}：展现某个特定环境变量和属性变量的值；
     * （7）GET /health：展现当前应用的健康状况；
     * （8）GET /info：展现应用特定的信息；
     * （9）GET /metrics：列出应用相关的指标，包括请求特定端点的运行次数；
     * （10）GET /metrics/{name}：展现应用特定指标项的指标状况；
     * （11）POST /shutdown：强制关闭应用；
     * （12）GET /trace：列出应用最近请求相关的元数据，包括请求和响应头。
     *
     * 为了启用 Actuator，只需将 Actuator Starter 依赖添加到项目中即可。如果你使用 Groovy 编写应用并通过
     * Spring Boot CLI 来运行，那么可以通过 @Grab 注解来添加 Actuator Starter，如下所示：
     *
     * @Grab("spring-boot-starter-actuator")
     *
     * 或者，在项目的 Maven pom.xml 文件中，可以添加如下的 <dependency>：
     *
     *         <dependency>
     *             <groupId>org.springframework.boot</groupId>
     *             <artifactId>spring-boot-starter-actuator</artifactId>
     *         </dependency>
     *
     * 添加完 Spring Boot Actuator 之后，可以重新构建并启动应用，然后打开浏览器访问以上所述的端点来获取更多
     * 信息。例如，如果想要查看 Spring 应用上下文中所有的 bean，那么可以访问 http://localhost:8080/beans。
     *
     * 所有的 bean 都包含在 "/beans" 端点所产生的 JSON 中。对于自动装配和自动配置所形成的神秘结果，这里提供
     * 了一种了解内部实现的手段。
     *
     * 另外一个端点也能帮助了解 Spring Boot 自动配置的内部情况，这就是 "/autoconfig"。这个端点所返回的
     * JSON 描述了 Spring Boot 在自动配置 bean 的时候所做出的决策。
     *
     * 所输出的报告包含了两部分：一部分是没有匹配上的（negative matches），另一部分是匹配上的（positive
     * matches）。在没有匹配的部分中，表明没有使用 AOP 和自动配置，因为在类路径中没有找到所需的类。在匹配
     * 上的部分中，可以看到，因为在类路径下找到了 SpringTemplateEngine，Thymeleaf 自动配置将会发挥作用。
     * 同时还可以看到，除非明确声明了默认的模板解析器、视图解析器以及模板 bean，否则的话，这些 bean 会进行
     * 自动配置。另外，只有在类路径中能够找到 Servlet 类，才会自动配置默认的视图解析器。
     *
     * "/beans" 和 "/autoconfig" 端点只是 Spring Boot Actuator 所提供的观察应用内部状况的两个样例。
     * 在这里，不会详细介绍每个端点，但是建议你自行尝试这些端点，以便掌握 Actuator 都提供了哪些功能来帮助
     * 了解应用的内部状况。
     */
    public static void main(String[] args) {

    }

}
