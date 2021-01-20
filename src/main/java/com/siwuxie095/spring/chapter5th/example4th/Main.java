package com.siwuxie095.spring.chapter5th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-01-20 21:23:05
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 搭建 Spring MVC
     *
     * 这里要使用最简单的方式来配置 Spring MVC：所要实现的功能仅限于运行所创建的控制器。
     *
     *
     *
     * 1、配置 DispatcherServlet
     *
     * DispatcherServlet 是 Spring MVC 的核心。在这里请求会第一次接触到框架，它要负责将请求路由到其他的组件之中。
     *
     * 按照传统的方式，像 DispatcherServlet 这样的 Servlet 会配置在 web.xml 文件中，这个文件会放到应用的 WAR 包里面。
     * 当然，这是配置 DispatcherServlet 的方法之一。但是，借助于 Servlet 3 规范和 Spring 3.1 的功能增强，这种方式已
     * 经不是唯一的方案了，这也不是这里所使用的配置方法。
     *
     * 这里会使用 Java 将 DispatcherServlet 配置在 Servlet 容器中，而不会再使用 web.xml 文件。
     *
     * 具体可参见 SpittrWebAppInitializer 类。这里暂且不纠结 spittr 到底是什么意思，后续会对其进行介绍，现在只需要知
     * 道所要创建的应用名为 Spittr。
     *
     * 要理解这段代码是如何工作的，可能只需要知道扩展 AbstractAnnotationConfigDispatcherServletInitializer 的任意
     * 类都会自动地配置 DispatcherServlet 和 Spring 应用上下文，Spring 的应用上下文会位于应用程序的 Servlet 上下文
     * 之中。
     *
     * 在 Servlet 3.0 环境中，容器会在类路径中查找实现 javax.servlet.ServletContainerInitializer 接口的类，如果能
     * 发现的话，就会用它来配置 Servlet 容器。
     *
     * Spring 提供了这个接口的实现，名为 SpringServletContainerInitializer，这个类反过来又会查找实现
     * WebApplicationInitializer 的类并将配置的任务交给它们来完成。Spring 3.2 引入了一个便利的 WebApplicationInitializer
     * 基础实现，也就是 AbstractAnnotationConfigDispatcherServletInitializer。因为这里 SpittrWebAppInitializer
     * 扩展了 AbstractAnnotationConfigDispatcherServletInitializer（同时也就实现了 WebApplicationInitializer），
     * 因此当部署到 Servlet 3.0 容器中的时候，容器会自动发现它，并用它来配置 Servlet 上下文。尽管它的名字很长，但是
     * AbstractAnnotationConfigDispatcherServlet-Initializer 使用起来很简便。
     *
     * SpittrWebAppInitializer 重写了三个方法。
     *
     * 第一个方法是 getServletMappings()，它会将一个或多个路径映射到 DispatcherServlet 上。在本例中，它映射的是 "/"，
     * 这表示它会是应用的默认 Servlet。它会处理进入应用的所有请求。
     *
     * 为了理解其他的两个方法，这里首先要理解 DispatcherServlet 和一个 Servlet 监听器（也就是 ContextLoaderListener）
     * 的关系。
     *
     *
     *
     * 2、两个应用上下文之间的故事
     *
     * 当 DispatcherServlet 启动的时候，它会创建 Spring 应用上下文，并加载配置文件或配置类中所声明的 bean。
     *
     * 在 getServletConfigClasses() 方法中，要求 DispatcherServlet 加载应用上下文时，使用定义在 WebConfig 配置类
     * （使用 Java 配置）中的 bean。
     *
     * 但是在 Spring Web 应用中，通常还会有另外一个应用上下文。另外的这个应用上下文是由 ContextLoaderListener 创建的。
     *
     * 这里希望 DispatcherServlet 加载包含 Web 组件的 bean，如控制器、视图解析器以及处理器映射，而 ContextLoaderListener
     * 要加载应用中的其他 bean。这些 bean 通常是驱动应用后端的中间层和数据层组件。
     *
     * 实际上，AbstractAnnotationConfigDispatcherServletInitializer 会同时创建 DispatcherServlet 和
     * ContextLoaderListener。getServletConfigClasses() 方法返回的带有 @Configuration 注解的类将会用来定义
     * DispatcherServlet 应用上下文中的 bean。getRootConfigClasses() 方法返回的带有 @Configuration 注解的类将会
     * 用来配置 ContextLoaderListener 创建的应用上下文中的 bean。
     *
     * 在本例中，根配置定义在 RootConfig 中，DispatcherServlet 的配置声明在 WebConfig 中。
     *
     * 需要注意的是，通过 AbstractAnnotationConfigDispatcherServletInitializer 来配置 DispatcherServlet 是传统
     * web.xml 方式的替代方案。当然，也可以同时包含 web.xml 和 AbstractAnnotationConfigDispatcherServletInitializer，
     * 但这其实并没有必要。
     *
     * 如果按照这种方式配置 DispatcherServlet，而不是使用 web.xml 的话，那唯一问题在于它只能部署到支持 Servlet 3.0 的
     * 服务器中才能正常工作，如 Tomcat 7 或更高版本。Servlet 3.0 规范在 2009 年 12 月份就发布了，因此很有可能你会将应用
     * 部署到支持 Servlet 3.0 的 Servlet 容器之中。
     *
     * 如果你还没有使用支持 Servlet 3.0 的服务器，那么在 AbstractAnnotationConfigDispatcherServletInitializer 子
     * 类中配置 DispatcherServlet 的方法就不适合你了。别无选择，只能使用 web.xml 了。
     *
     * 下面看一下所引用的 WebConfig 和 RootConfig，了解一下如何启用 Spring MVC。
     *
     *
     *
     * 3、启用 Spring MVC
     *
     * 可以有多种方式来配置 DispatcherServlet，与之类似，启用 Spring MVC 组件的方法也不仅一种。以前，Spring 是使用 XML
     * 进行配置的，你可以使用 <mvc:annotation-driven> 启用注解驱动的 Spring MVC。
     *
     * 后续会在介绍 Spring MVC 配置可选项的时候，再讨论 <mvc:annotation-driven>。不过，现在会让 Spring MVC 的搭建过程
     * 尽可能简单并基于 Java 进行配置。
     *
     * 这里所能创建的最简单的 Spring MVC 配置就是一个带有 @EnableWebMvc 注解的类：
     *
     * @Configuration
     * @EnableWebMvc
     * public class WebConfig extends WebMvcConfigurerAdapter {
     *
     * }
     *
     * 这可以运行起来，它的确能够启用 Spring MVC，但还有不少问题要解决：
     * （1）没有配置视图解析器。如果这样的话，Spring 默认会使用 BeanNameViewResolver，这个视图解析器会查找 ID 与视图名称
     * 匹配的 bean，并且查找的 bean 要实现 View 接口，它以这样的方式来解析视图。
     * （2）没有启用组件扫描。这样的结果就是，Spring 只能找到显式声明在配置类中的控制器。
     * （3）这样配置的话，DispatcherServlet 会映射为应用的默认 Servlet，所以它会处理所有的请求，包括对静态资源的请求，如
     * 图片和样式表（在大多数情况下，这可能并不是你想要的效果）。
     *
     * 因此，需要在 WebConfig 这个最小的 Spring MVC 配置上再加一些内容，从而让它变得真正有用。
     *
     * @Configuration
     * @EnableWebMvc
     * @ComponentScan("com.siwuxie095.spring.chapter5th.example4th.web")
     * public class WebConfig extends WebMvcConfigurerAdapter {
     *
     *     // 配置 JSP 视图解析器
     *     @Bean
     *     public ViewResolver viewResolver() {
     *         InternalResourceViewResolver resolver = new InternalResourceViewResolver();
     *         resolver.setPrefix("/WEB-INF/views/");
     *         resolver.setSuffix(".jsp");
     *         resolver.setExposeContextBeansAsAttributes(true);
     *         return resolver;
     *     }
     *
     *     // 配置静态资源的处理
     *     @Override
     *     public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
     *         configurer.enable();
     *     }
     *
     * }
     *
     * 第一件需要注意的事情是 WebConfig 添加了 @ComponentScan 注解，因此将会扫描包来查找组件。
     *
     * 接下来，添加了一个 ViewResolver bean。更具体来讲，是 InternalResourceViewResolver。它会查找 JSP 文件，在查找的
     * 时候，它会在视图名称上加一个特定的前缀和后缀（例如，名为 home 的视图将会解析为 /WEB-INF/views/home.jsp）。
     *
     * 最后，新的 WebConfig 类还扩展了 WebMvcConfigurerAdapter 并重写了其 configureDefaultServletHandling() 方法。
     * 通过调用 DefaultServletHandlerConfigurer 的 enable() 方法，要求 DispatcherServlet 将对静态资源的请求转发到
     * Servlet 容器中默认的 Servlet 上，而不是使用 DispatcherServlet 本身来处理此类请求。
     *
     * WebConfig 已经就绪，那 RootConfig 呢？因为这里聚焦于 Web 开发，而 Web 相关的配置通过 DispatcherServlet 创建的
     * 应用上下文都已经配置好了，因此现在的 RootConfig 相对很简单：
     *
     * @Configuration
     * @ComponentScan(basePackages = {"com.siwuxie095.spring.chapter5th.example4th"},
     *         excludeFilters = {
     *                 @ComponentScan.Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class)
     *         })
     * public class RootConfig {
     *
     *
     * }
     *
     * 唯一需要注意的是 RootConfig 使用了 @ComponentScan 注解。这样的话，后续就有很多机会用非 Web 的组件来充实并完善
     * RootConfig。
     *
     * 现在，基本上已经可以开始使用 Spring MVC 构建 Web 应用了。
     */
    public static void main(String[] args) {

    }

}
