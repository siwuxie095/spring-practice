package com.siwuxie095.spring.chapter7th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-01-31 19:08:51
 */
@SuppressWarnings("all")
public class Main {

    /**
     * Spring MVC 配置的替代方案
     *
     * 之前通过扩展 AbstractAnnotationConfigDispatcherServletInitializer 快速搭建了 Spring MVC 环境。在这个便利的
     * 基础类中，假设需要基本的 DispatcherServlet 和 ContextLoaderListener 环境，并且 Spring 配置是使用 Java 的，而
     * 不是 XML。
     *
     * 尽管对很多 Spring 应用来说，这是一种安全的假设，但是并不一定总能满足要求。除了 DispatcherServlet 以外，可能还需要
     * 额外的 Servlet 和 Filter；可能还需要对 DispatcherServlet 本身做一些额外的配置。或者，如果需要将应用部署到 Servlet
     * 3.0 之前的容器中，那么还需要将 DispatcherServlet 配置到传统的 web.xml 中。
     *
     *
     *
     * 自定义 DispatcherServlet 配置
     *
     * 虽然从外观上不一定能够看得出来，但是 AbstractAnnotationConfigDispatcherServletInitializer 所完成的事情其实比
     * 看上去要多。在 SpittrWebAppInitializer 中所编写的三个方法仅仅是必须要重载的 abstract 方法。但实际上还有更多的方
     * 法可以进行重载，从而实现额外的配置。
     *
     * 此类的方法之一就是 customizeRegistration()。在 AbstractAnnotation-ConfigDispatcherServletInitializer 将
     * DispatcherServlet 注册到 Servlet 容器中之后，就会调用 customizeRegistration()，并将 Servlet 注册后得到的
     * Registration.Dynamic 传递进来。通过重载 customizeRegistration() 方法，可以对 DispatcherServlet 进行额外的
     * 配置。
     *
     * 例如，在 Spring MVC 中处理 multipart 请求和文件上传时，如果计划使用 Servlet 3.0 对 multipart 配置的支持，那么
     * 需要使用 DispatcherServlet 的 registration 来启用 multipart 请求。可以重载 customizeRegistration() 方法来
     * 设置 MultipartConfigElement，如下所示：
     *
     *     @Override
     *     protected void customizeRegistration(ServletRegistration.Dynamic registration) {
     *         registration.setMultipartConfig(new MultipartConfigElement("tmp/spittr/uploads"));
     *     }
     *
     * 借助 customizeRegistration() 方法中的 ServletRegistration.Dynamic，能够完成多项任务，包括：
     * （1）通过调用 setLoadOnStartup() 设置 load-on-startup 优先级；
     * （2）通过调用 setInitParameter() 设置初始化参数；
     * （3）通过调用 setMultipartConfig() 配置 Servlet 3.0 对multipart的支持；
     * （4）...
     *
     * 在前面的样例中，设置了对 multipart 的支持，将上传文件的临时存储目录 设置在 "/tmp/spittr/uploads" 中。
     *
     *
     *
     * 添加其他的 Servlet 和 Filter
     *
     * 按照 AbstractAnnotationConfigDispatcherServletInitializer 的定义，它会创建 DispatcherServlet 和
     * ContextLoaderListener。但是，如果你想注册其他的 Servlet、Filter 或 Listener 的话，那该怎么办呢？
     *
     * 基于 Java 的初始化器（initializer）的一个好处就在于可以定义任意数量的初始化器类。因此，如果想往 Web 容器中注册其他
     * 组件的话，只需创建一个新的初始化器就可以了。最简单的方式就是实现 Spring 的 WebApplicationInitializer 接口。
     *
     * 例如，如下的代码展现了如何创建 WebApplicationInitializer 实现并注册一个 Servlet。
     *
     * public class MyServletInitializer implements WebApplicationInitializer {
     *
     *     @Override
     *     public void onStartup(ServletContext servletContext) throws ServletException {
     *         ServletRegistration.Dynamic myServlet =
     *                 servletContext.addServlet("myServlet", MyServlet.class);
     *         myServlet.addMapping("/custom/**");
     *     }
     *
     * }
     *
     * 这是相当基础的 Servlet 注册初始化器类。它注册了一个 Servlet 并将其映射到一个路径上。也可以通过这种方式来手动注册
     * DispatcherServlet。但这并没有必要，因为 AbstractAnnotationConfigDispatcherServletInitializer 没用太多代码
     * 就将这项任务完成得很漂亮。
     *
     * 类似地，还可以创建新的 WebApplicationInitializer 实现来注册 Listener 和 Filter。
     *
     * 例如，如下的代码展现了如何注册 Filter。
     *
     * public class MyFilterInitializer implements WebApplicationInitializer {
     *
     *     @Override
     *     public void onStartup(ServletContext servletContext) throws ServletException {
     *         FilterRegistration.Dynamic myFilter =
     *                 servletContext.addFilter("myFilter", MyFilter.class);
     *         myFilter.addMappingForUrlPatterns(null, false, "/custom/*");
     *     }
     *
     * }
     *
     * 如果要将应用部署到支持 Servlet 3.0 的容器中，那么 WebApplicationInitializer 提供了一种通用的方式，实现在 Java
     * 中注册 Servlet、Filter 和 Listener。不过，如果你只是注册 Filter，并且该 Filter 只会映射到 DispatcherServlet
     * 上的话，那么在 AbstractAnnotationConfigDispatcherServletInitializer 中还有一种快捷方式。为了注册 Filter 并
     * 将其映射到 DispatcherServlet，所需要做的仅仅是重载 AbstractAnnotationConfigDispatcherServletInitializer
     * 的 getServletFilters() 方法。例如，在如下的代码中，重载了 getServletFilters() 方法以注册 Filter：
     *
     *     @Override
     *     protected Filter[] getServletFilters() {
     *         return new Filter[] { new MyFilter() };
     *     }
     *
     * 可以看到，这个方法返回的是一个 javax.servlet.Filter 的数组。在这里它只返回了一个 Filter，但它实际上可以返回任意
     * 数量的 Filter。在这里没有必要声明它的映射路径，getServletFilters() 方法返回的所有 Filter 都会映射到
     * DispatcherServlet 上。
     *
     * 如果要将应用部署到 Servlet 3.0 容器中，那么 Spring 提供了多种方式来注册 Servlet（包括 DispatcherServlet）、
     * Filter 和 Listener，而不必创建 web.xml 文件。但是，如果你不想采取以上所述方案的话，也是可以的。假设你需要将应用
     * 部署到不支持 Servlet 3.0 的容器中（或者你只是希望使用 web.xml 文件），那么完全可以按照传统的方式，通过 web.xml
     * 配置 Spring MVC。下面看一下该怎么做。
     *
     *
     *
     * 在 web.xml 中声明 DispatcherServlet
     *
     * 在典型的 Spring MVC 应用中，会需要 DispatcherServlet 和 ContextLoaderListener。
     *
     * AbstractAnnotationConfigDispatcherServletInitializer 会自动注册它们，但是如果需要在 web.xml 中注册的话，
     * 那就需要自己来完成这项任务了。
     *
     * 如下是一个基本的 web.xml 文件，它按照传统的方式搭建了 DispatcherServlet 和 ContextLoaderListener。
     *
     * <!-- 设置根上下文配置文件位置 -->
     * <context-param>
     *     <param-name>contextConfigLocation</param-name>
     *     <param-value>/WEB-INF/spring/root-context.xml</param-value>
     * </context-param>
     *
     * <!-- 注册 ContextLoaderListener -->
     * <listener>
     *     <listener-class>
     *         org.springframework.web.context.ContextLoaderListener
     *     </listener-class>
     * </listener>
     *
     * <!-- 注册 DispatcherServlet -->
     * <servlet>
     *     <servlet-name>appServlet</servlet-name>
     *     <servlet-class>
     *         org.springframework.web.servlet.DispatcherServlet
     *     </servlet-class>
     *     <load-on-startup>1</load-on-startup>
     * </servlet>
     *
     * <!-- 将 DispatcherServlet 映射到 "/" -->
     * <servlet-mapping>
     *     <servlet-name>appServlet</servlet-name>
     *     <url-pattern>/</url-pattern>
     * </servlet-mapping>
     *
     * ContextLoaderListener 和 DispatcherServlet 各自都会加载一个 Spring 应用上下文。
     *
     * 上下文参数 contextConfigLocation 指定了一个 XML 文件的地址，这个文件定义了根应用上下文，它会被
     * ContextLoaderListener 加载。根上下文会从 "/WEB-INF/spring/root-context.xml" 中加载 bean
     * 定义。
     *
     * DispatcherServlet 会根据 Servlet 的名字找到一个文件，并基于该文件加载应用上下文。这里 Servlet
     * 的名字是 appServlet，因此 DispatcherServlet 会从 "/WEB-INF/appServlet-context.xml" 文件
     * 中加载其应用上下文。如果你希望指定 DispatcherServlet 配置文件的位置的话，那么可以在 Servlet 上
     * 指定一个 contextConfigLocation 初始化参数。例如，如下的配置中，DispatcherServlet 会从
     * "/WEB-INF/spring/appServlet/servlet-context.xml" 加载它的 bean。
     *
     * <servlet>
     *     <servlet-name>appServlet</servlet-name>
     *     <servlet-class>
     *         org.springframework.web.servlet.DispatcherServlet
     *     </servlet-class>
     *     <init-param>
     *         <param-name>contextConfigLocation</param-name>
     *         <param-value>
     *             /WEB-INF/spring/appServlet/servlet-context.xml
     *         </param-value>
     *     </init-param>
     *     <load-on-startup>1</load-on-startup>
     * </servlet>
     *
     * 当然，上面阐述的都是如何让 DispatcherServlet 和 ContextLoaderListener 从 XML 中加载各自的应用上下文。但是，
     * 这里更倾向于使用 Java 配置而不是 XML 配置。因此，需要让 Spring MVC 在启动的时候，从带有 @Configuration 注解
     * 的类上加载配置。
     *
     * 要在 Spring MVC 中使用基于 Java 的配置，需要告诉 DispatcherServlet 和 ContextLoaderListener 使用
     * AnnotationConfigWebApplicationContext，这是一个 WebApplicationContext 的实现类，它会加载 Java 配置类，
     * 而不是使用 XML。要实现这种配置，可以设置 contextClass 上下文参数以及 DispatcherServlet 的初始化参数。如下的
     * 代码展现了一个新的 web.xml，在这个文件中，它所搭建的 Spring MVC 使用基于 Java 的 Spring 配置：
     *
     * <context-param>
     *     <param-name>contextClass</param-name>
     *     <param-value>
     *         org.springframework.web.context.support.AnnotationConfigWebApplicationContext
     *     </param-value>
     * </context-param>
     *
     * <context-param>
     *     <param-name>contextConfigLocation</param-name>
     *     <param-value>com.siwuxie095.spring.chapter7th.example2nd.cfg.RootConfig</param-value>
     * </context-param>
     *
     * <listener>
     *     <listener-class>
     *         org.springframework.web.context.ContextLoaderListener
     *     </listener-class>
     * </listener>
     *
     * <servlet>
     *     <servlet-name>appServlet</servlet-name>
     *     <servlet-class>
     *         org.springframework.web.servlet.DispatcherServlet
     *     </servlet-class>
     *     <init-param>
     *         <param-name>contextClass</param-name>
     *         <param-value>
     *             org.springframework.web.context.support.AnnotationConfigWebApplicationContext
     *         </param-value>
     *     </init-param>
     *     <init-param>
     *         <param-name>contextConfigLocation</param-name>
     *         <param-value>
     *             com.siwuxie095.spring.chapter7th.example2nd.web.WebConfig
     *         </param-value>
     *     </init-param>
     *     <load-on-startup>1</load-on-startup>
     * </servlet>
     *
     * <servlet-mapping>
     *     <servlet-name>appServlet</servlet-name>
     *     <url-pattern>/</url-pattern>
     * </servlet-mapping>
     *
     * 现在已经看到了如何以多种不同的方式来搭建 Spring MVC，那么后续会看一下如何使用 Spring MVC 来处理文件上传。
     */
    public static void main(String[] args) {

    }

}
