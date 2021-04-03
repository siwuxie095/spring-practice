package com.siwuxie095.spring.chapter21th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-04-02 22:00:37
 */
@SuppressWarnings("all")
public class Main {

    /**
     * Spring Boot 简介
     *
     * 在 Spring 家族中，Spring Boot 是令人兴奋（也许它是改变游戏规则的）的新项目。它提供了四个主要的特性，能够改变
     * 开发 Spring 应用程序的方式：
     * （1）Spring Boot Starter：它将常用的依赖分组进行了整合，将其合并到一个依赖中，这样就可以一次性添加到项目的
     * Maven 或 Gradle 构建中；
     * （2）自动配置：Spring Boot 的自动配置特性利用了 Spring 4 对条件化配置的支持，合理地推测应用所需的 bean 并
     * 自动化配置它们；
     * （3）命令行接口（Command-line interface，CLI）：Spring Boot 的 CLI 发挥了 Groovy 编程语言的优势，并结合
     * 自动配置进一步简化 Spring 应用的开发；
     * （4）Actuator：它为 Spring Boot 应用添加了一定的管理特性。
     *
     * 在这里，将会使用 Spring Boot 的所有特性构建一个小型的应用程序。但首先，来快速了解一下每项特性，更好地体验它们
     * 如何简化 Spring 编程模型。
     *
     *
     *
     * 1、添加 Starter 依赖
     *
     * 有两种烤制蛋糕的方式，有热情的人会将面粉、鸡蛋、糖、发酵粉、盐、奶油、香草调料以及牛奶混合在一起，和成糊状。或者
     * 也可以购买预先打包好的蛋糕，它包含了所需的大部分原料，只需添加一些含水分的材料即可，如水、鸡蛋和植物油。
     *
     * 预先打包好的蛋糕将制作蛋糕过程中所需的各种材料集合在了一起，作为一项材料来使用，与之类似，Spring Boot Starter
     * 将应用所需的各种依赖聚合成一项依赖。
     *
     * 为了阐述该功能，假设要从头开始编写一个新的 Spring 应用。这是一个 Web 项目，所以需要使用 Spring MVC。同时，还
     * 要有 REST API 将资源暴露为 JSON，所以在构建中需要包含 Jackson JSON 库。
     *
     * 因为应用需要使用 JDBC 从关系型数据库中存储和查询数据，因此希望确保包含了 Spring 的 JDBC 模块（为了使用
     * JdbcTemplate）和 Spring 的事务模块（为了使用声明式事务的支持）。对于数据库本身，H2 数据库是个不错的选择。
     *
     * 对了，还需要使用 Thymeleaf 来建立 Spring MVC 视图。
     *
     * Spring Boot 提供了多个 Starter，如下是所有可用的 Starter：
     * （1）spring-boot-starter-actuator：
     * spring-boot-starter、spring-boot-actuator、spring-core
     * （2）spring-boot-starter-amqp：
     * spring-boot-starter、spring-boot-rabbit、spring-core、spring-tx
     * （3）spring-boot-starter-aop：
     * spring-boot-starter、spring-aop、AspectJ Runtime、AspectJ Weaver、spring-core
     * （4）spring-boot-starter-batch：
     * spring-boot-starter、HSQLDB、spring-jdbc、spring-batch-core、spring-core
     * （5）spring-boot-starter-elasticsearch：
     * spring-boot-starter、spring-data-elasticsearch、spring-core、spring-tx
     * （6）spring-boot-starter-gemfire：
     * spring-boot-starter、Gemfire、spring-core、spring-tx、spring-context、spring-context-support、
     * spring-data-gemfire
     * （7）spring-boot-starter-data-jpa：
     * spring-boot-starter、spring-boot-starter-jdbc、spring-boot-starter-aop、spring-core、Hibernate
     * EntityManager、spring-orm、spring-data-jpa、spring-aspects
     * （8）spring-boot-starter-data-mongodb：
     * spring-boot-starter、MongoDB Java 驱动、spring-core、spring-tx、spring-data-mongodb
     * （9）spring-boot-starter-data-rest：
     * spring-boot-starter、spring-boot-starter-web、Jackson 注解、Jackson 数据绑定、spring-core、
     * spring-tx、spring-data-rest-webmvc
     * （10）spring-boot-starter-data-solr：
     * spring-boot-starter、Solrj、spring-core、spring-tx、spring-data-solr、Apache HTTP Mime
     * （11）spring-boot-starter-freemarker：
     * spring-boot-starter、spring-boot-starter-web、Freemarker、spring-core、spring-context-support
     * （12）spring-boot-starter-groovy-templates：
     * spring-boot-starter、spring-boot-starter-web、Groovy、Groovy 模板、spring-core
     * （13）spring-boot-starter-hornetq：
     * spring-boot-starter、spring-core、spring-jms、Hornet JMS Client
     * （14）spring-boot-starter-integration：
     * spring-boot-starter、spring-aop、spring-tx、spring-web、spring-webmvc、spring-integration-core、
     * spring-integration-file、spring-integration-http、spring-integration-ip、spring-integration-stream
     * （15）spring-boot-starter-jdbc：
     * spring-boot-starter、spring-jdbc、tomcat-jdbc、spring-tx
     * （16）spring-boot-starter-jetty：
     * jetty-webapp、jetty-jsp
     * （17）spring-boot-starter-log4j：
     * jcl-over-slf4j、jul-to-slf4j、slf4j-log4j12、log4j
     * （18）spring-boot-starter-logging：
     * jcl-over-slf4j、jul-to-slf4j、log4j-over-slf4j、logback-classic
     * （19）spring-boot-starter-mobile：
     * spring-boot-starter、spring-boot-starter-web、spring-mobile-device
     * （20）spring-boot-starter-redis：
     * spring-boot-starter、spring-data-redis、lettuce
     * （21）spring-boot-starter-remote-shell：
     * spring-boot-starter-actuator、spring-context、org.crashub.**
     * （22）spring-boot-starter-security：
     * spring-boot-starter、spring-security-config、spring-security-web、spring-aop、spring-beans、
     * spring-context、spring-core、spring-expression、spring-web
     * （23）spring-boot-starter-social-facebook：
     * spring-boot-starter、spring-boot-starter-web、spring-core、spring-social-config、
     * spring-social-core、spring-social-web、spring-social-facebook
     * （24）spring-boot-starter-social-twitter：
     * spring-boot-starter、spring-boot-starter-web、spring-core、spring-social-config、
     * spring-social-core、spring-social-web、spring-social-twitter
     * （25）spring-boot-starter-social-linkedin：
     * spring-boot-starter、spring-boot-starter-web、spring-core、spring-social-config、
     * spring-social-core、spring-social-web、spring-social-linkedin
     * （26）spring-boot-starter：
     * spring-boot、spring-boot-autoconfigure、spring-boot-starter-logging
     * （27）spring-boot-starter-test：
     * spring-boot-starter-logging、spring-boot、junit、mockito-core、hamcrest-library、spring-test
     * （28）spring-boot-starter-thymeleaf：
     * spring-boot-starter、spring-boot-starter-web、spring-core、thymeleaf-spring4、
     * thymeleaf-layout-dialect
     * （29）spring-boot-starter-tomcat：
     * tomcat-embed-core、tomcat-embed-logging-juli
     * （30）spring-boot-starter-web：
     * spring-boot-starter、spring-boot-starter-tomcat、jackson-databind、spring-web、spring-webmvc
     * （31）spring-boot-starter-websocket：
     * spring-boot-starter-web、spring-websocket、tomcat-embed-core、tomcat-embed-logging-juli
     * （32）spring-boot-starter-ws：
     * spring-boot-starter、spring-boot-starter-web、spring-core、spring-jms、spring-oxm、spring-ws-core、
     * spring-ws-support
     *
     * PS：Spring Boot Starter 依赖将所需的常见依赖按组聚集在一起，形成单条依赖。
     *
     * 如果查看这些 Starter 依赖的内部原理，你会发现 Starter 的工作方式也没有什么神秘之处。它使用了 Maven 和 Gradle
     * 的依赖传递方案，Starter 在自己的 pom.xml 文件中声明了多个依赖。当将某一个 Starter 依赖添加到 Maven 或 Gradle
     * 构建中的时候，Starter 的依赖将会自动地传递性解析。这些依赖本身可能也会有其他的依赖。一个 Starter 可能会传递性
     * 地引入几十个依赖。
     *
     * 需要注意，很多 Starter 引用了其他的 Starter。例如，mobile Starter 就引用了 Web Starter，而后者又引用了
     * Tomcat Starter。大多数的 Starter 都会引用 spring-boot-starter，它实际上是一个基础的 Starter（当然，它
     * 也依赖了 logging Starter）。依赖是传递性的，将 mobile Starter 添加为依赖之后，就相当于添加了它下面的所有
     * Starter。
     *
     *
     *
     * 2、自动配置
     *
     * Spring Boot 的 Starter 减少了构建中依赖列表的长度，而 Spring Boot 的自动配置功能则削减了 Spring 配置的数量。
     * 它在实现时，会考虑应用中的其他因素并推断你所需要的 Spring 配置。
     *
     * 作为样例，比如要将 Thymeleaf 模板作为 Spring MVC 的视图，至少需要三个 bean：ThymeleafViewResolver、
     * SpringTemplateEngine 和 TemplateResolver。但是，使用 Spring Boot 自动配置的话，需要做的仅仅是将
     * Thymeleaf 添加到项目的类路径中。如果 Spring Boot 探测到 Thymeleaf 位于类路径中，它就会推断这里需要
     * 使用 Thymeleaf 实现 Spring MVC 的视图功能，并自动配置这些 bean。
     *
     * Spring Boot Starter 也会触发自动配置。例如，在 Spring Boot 应用中，如果想要使用 Spring MVC 的话，所需要做
     * 的仅仅是将 Web Starter 作为依赖放到构建之中。将 Web Starter 作为依赖放到构建中以后，它会自动添加 Spring MVC
     * 依赖。如果 Spring Boot 的 Web 自动配置探测到 Spring MVC 位于类路径下，它将会自动配置支持 Spring MVC 的多个
     * bean，包括视图解析器、资源处理器以及消息转换器（等等）。接下来需要做的就是编写处理请求的控制器。
     *
     *
     *
     * 3、Spring Boot CLI
     *
     * Spring Boot CLI 充分利用了 Spring Boot Starter 和自动配置的魔力，并添加了一些 Groovy 的功能。它简化了
     * Spring 的开发流程，通过 CLI，能够运行一个或多个 Groovy 脚本，并查看它是如何运行的。在应用的运行过程中，CLI
     * 能够自动导入 Spring 类型并解析依赖。
     *
     * 用来阐述 Spring Boot CLI 的最有趣的例子就是如下的 Groovy 脚本：
     *
     * @RestController
     * class Hi {
     *
     *      @RequestMapping("/")
     *      String hi() {
     *          "Hi!"
     *      }
     *
     * }
     *
     * 不管你是否相信，这是一个完整的（尽管比较简单）Spring 应用，它可以在 Spring Boot CLI 中运行。包括空格，它的长度
     * 只有 82 个字符。
     *
     * 去掉不必要的空格，我们能够得到只有 64 个字符的一行代码：
     *
     * @RestController class Hi{@RequestMapping("/") String hi(){"Hi!"}}
     *
     * 这个版本更加简单，但它依然是一个完整可运行的（尽管特性比较简陋）Spring 应用。如果你已经安装过 Spring Boot CLI，
     * 可以使用如下的命令行来运行它：
     *
     * spring run Hi.groovy
     *
     *
     *
     * 4、Actuator
     *
     * Spring Boot Actuator 为 Spring Boot 项目带来了很多有用的特性，包括：
     * （1）管理端点；
     * （2）合理的异常处理以及默认的 "/error" 映射端点；
     * （3）获取应用信息的 "/info" 端点；
     * （4）当启用 Spring Security 时，会有一个审计事件框架。
     *
     * 这些特性都是很有用的，但 Actuator 最有用和最有意思的特性是管理端点。后续将会看到 Spring Boot Actuator 的几个
     * 样例，它开启了一扇窗，能够让开发者洞悉应用的内部运行状况。
     */
    public static void main(String[] args) {

    }

}
