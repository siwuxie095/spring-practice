package com.siwuxie095.spring.chapter1st.example11th;

/**
 * @author Jiajing Li
 * @date 2020-12-20 11:05:13
 */
public class Main {

    /**
     * Spring 的新功能
     *
     * Spring 框架经历了 3 个重要的发布版本 —— 3.1、3.2 以及现在的 4.0 —— 每个版本都带来了新的
     * 特性和增强，以简化应用程序的研发。Spring Portfolio 中的一些成员项目也经历了重要的变更。
     *
     * 下面简要地了解一下 Spring 带来了哪些新功能。
     *
     *
     * Spring 3.1 新特性
     *
     * Spring 3.1 带来了多项有用的新特性和增强，其中有很多都是关于如何简化和改善配置的。除此之外，
     * Spring 3.1 还提供了声明式缓存的支持以及众多针对 Spring MVC 的功能增强。下面的列表展现了
     * Spring 3.1 重要的功能升级：
     * （1）为了解决各种环境下（如开发、测试和生产）选择不同配置的问题，Spring 3.1 引入了环境
     * profile 功能。借助于 profile，就能根据应用部署在什么环境之中选择不同的数据源 bean；
     * （2）在 Spring 3.0 基于 Java 的配置之上，Spring 3.1 添加了多个 enable 注解，这样就能
     * 使用这个注解启用 Spring 的特定功能（即 Enable 开头的注解）；
     * （3）添加了 Spring 对声明式缓存的支持，能够使用简单的注解声明缓存边界和规则，这和以前声明
     * 事务边界很类似；
     * （4）新添加的用于构造器注入的 c 命名空间，它类似于 Spring 2.0 所提供的面向属性的 p 命名
     * 空间，p 命名空间用于属性注入，它们都是非常简洁易用的（即 对应 Constructor 和 Property）；
     * （5）Spring 开始支持 Servlet 3.0，包括在基于 Java 的配置中声明 Servlet 和 Filter，而
     * 不再借助于 web.xml；
     * （6）改善 Spring 对 JPA 的支持，使得它能够在 Spring 中完整地配置 JPA，不必再使用
     * persistence.xml 文件。
     *
     * Spring 3.1 还包含了多项针对 Spring MVC 的功能增强：
     * （1）自动绑定路径变量到模型属性中；
     * （2）提供了 @RequestMapping 的 produces 和 consumes 属性，用于匹配请求中的 Accept 和
     * Content-Type 头部信息；
     * （3）提供了 @RequestPart 注解，用于将 multipart 请求中的某些部分绑定到处理器的方法参数中；
     * （4）支持 Flash 属性（在 redirect 请求之后依然能够存活的属性）以及用于在请求间存放 flash
     * 属性的 RedirectAttributes 类型。
     *
     * 除了 Spring 3.1 所提供的新功能以外，同等重要的是要注意 Spring 3.1 不再支持的功能。具体来
     * 讲，为了支持原生的 EntityManager，Spring 的 JpaTemplate 和 JpaDaoSupport 类被废弃掉
     * 了。尽管它们已经被废弃了，但直到 Spring 3.2 版本，它依然是可以使用的。但最好不要再使用它们
     * 了，因为它们不会进行更新以支持 JPA 2.0，并且已经在 Spring 4 中移除掉了。
     *
     *
     * Spring 3.2 新特性
     *
     * Spring 3.1 在很大程度上聚焦于配置改善以及其他的一些增强，包括 Spring MVC 的增强，而 Spring
     * 3.2 是主要关注 Spring MVC 的一个发布版本。Spring MVC 3.2 带来了如下的功能提升：
     * （1）Spring 3.2 的控制器（Controller）可以使用 Servlet 3.0 的异步请求，允许在一个独立的
     * 线程中处理请求，从而将 Servlet 线程解放出来处理更多的请求；
     * （2）尽管从 Spring 2.5 开始，Spring MVC 控制器就能以 POJO 的形式进行很便利地测试，但是
     * Spring 3.2 引入了 Spring MVC 测试框架，用于为控制器编写更为丰富的测试，断言它们作为控制器
     * 的行为行为是否正确，而且在使用的过程中并不需要 Servlet 容器；
     * （3）除了提升控制器的测试功能，Spring 3.2 还包含了基于 RestTemplate 的客户端的测试支持，
     * 在测试的过程中，不需要往真正的 REST 端点上发送请求；
     * （4）@ControllerAdvice 注解能够将通用的 @ExceptionHandler、@InitBinder 和 @ModelAttributes
     * 方法收集到一个类中，并应用到所有控制器上；
     * （5）在 Spring 3.2 之前，只能通过 ContentNegotiatingViewResolver 使用完整的内容协商
     * （full content negotiation）功能。但是在 Spring 3.2 中，完整的内容协商功能可以在整个
     * Spring MVC 中使用，即便是依赖于消息转换器（message converter）使用和产生内容的控制器方
     * 法也能使用该功能；
     * （6）Spring MVC 3.2 包含了一个新的 @MatrixVariable 注解，这个注解能够将请求中的矩阵变量
     * （matrix variable）绑定到处理器的方法参数中；
     * （7）基础的抽象类 AbstractDispatcherServletInitializer 能够非常便利地配置 DispatcherServlet，
     * 而不必再使用 web.xml。与之类似，当你希望通过基于 Java 的方式来配置 Spring 的时候，可以使
     * 用 AbstractAnnotationConfigDispatcherServletInitializer 的子类；
     * （8）新增了 ResponseEntityExceptionHandler，可以用来替代 DefaultHandlerExceptionResolver。
     * ResponseEntityExceptionHandler 方法会返回 ResponseEntity<Object>，而不是 ModelAndView；
     * （9）RestTemplate 和 @RequestBody 的参数可以支持泛型；
     * （10）RestTemplate 和 @RequestMapping 可以支持 HTTP PATCH 方法；
     * （11）在拦截器匹配时，支持使用 URL 模式将其排除在拦截器的处理功能之外；
     *
     * 虽然 Spring MVC 是 Spring 3.2 改善的核心内容，但是它依然还增加了多项非 MVC 的功能改善。
     * 下面列出了 Spring 3.2 中几项最为有意思的新特性：
     * （1）@Autowired、@Value 和 @Bean 注解能够作为元注解，用于创建自定义的注入和 bean 声明注解；
     * （2）@DateTimeFormat 注解不再强依赖 JodaTime。如果提供了 JodaTime，就会使用它，否则的话，
     * 会使用 SimpleDateFormat；
     * （3）Spring 的声明式缓存提供了对 JCache 0.5 的支持；
     * （4）支持定义全局的格式来解析和渲染日期与时间；
     * （5）在集成测试中，能够配置和加载 WebApplicationContext；
     * （6）在集成测试中，能够针对 request 和 session 作用域的 bean 进行测试。
     *
     *
     * Spring 4.0 新特性
     *
     * 在 Spring 4.0 中包含了很多令人兴奋的新特性，包括：
     * （1）Spring 提供了对 WebSocket 编程的支持，包括支持JSR-356 —— Java API for WebSocket；
     * （2）鉴于 WebSocket 仅仅提供了一种低层次的 API，急需高层次的抽象，因此 Spring 4.0 在
     * WebSocket 之上提供了一个高层次的面向消息的编程模型，该模型基于 SockJS，并且包含了对 STOMP
     * 协议的支持；
     * （3）新的消息（messaging）模块，很多的类型来源于 Spring Integration 项目。这个消息模块支
     * 持 Spring 的 SockJS/STOMP 功能，同时提供了基于模板的方式发布消息；
     * （4）Spring 是第一批（如果不说是第一个的话）支持 Java 8 特性的 Java 框架，比如它所支持的
     * lambda表达式。别的暂且不说，这首先能够让使用特定的回调接口（如 RowMapper 和 JdbcTemplate）
     * 更加简洁，代码更加易读；
     * （5）与 Java 8 同时得到支持的是 JSR-310 —— Date 与 Time API，在处理日期和时间时，它为开
     * 发者提供了比 java.util.Date 或 java.util.Calendar 更丰富的 API；
     * （6）为 Groovy 开发的应用程序提供了更加顺畅的编程体验，尤其是支持非常便利地完全采用 Groovy
     * 开发 Spring 应用程序。随这些一起提供的是来自于 Grails 的 BeanBuilder，借助它能够通过
     * Groovy 配置 Spring 应用；
     * （7）添加了条件化创建 bean 的功能，在这里只有开发人员定义的条件满足时，才会创建所声明的 bean；
     * （8）Spring 4.0 包含了 Spring RestTemplate 的一个新的异步实现，它会立即返回并且允许在操作
     * 完成后执行回调；
     * （9）添加了对多项 JEE 规范的支持，包括 JMS 2.0、JTA 1.2、JPA 2.1 和 Bean Validation 1.1。
     */
    public static void main(String[] args) {

    }

}
