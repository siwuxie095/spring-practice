package com.siwuxie095.spring.chapter3rd.example4th;

/**
 * @author Jiajing Li
 * @date 2021-01-04 21:39:51
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 激活 profile
     *
     * Spring 在确定哪个 profile 处于激活状态时，需要依赖两个独立的属性：spring.profiles.active 和
     * spring.profiles.default。如果设置了 spring.profiles.active 属性的话，那么它的值就会用来确定
     * 哪个 profile 是激活的。但如果没有设置 spring.profiles.active 属性的话，那 Spring 将会查找
     * spring.profiles.default 的值。如果 spring.profiles.active 和 spring.profiles.default 均
     * 没有设置的话，那就没有激活的 profile，因此只会创建那些没有定义在 profile 中的 bean。
     *
     * 有多种方式来设置这两个属性：
     * （1）作为 DispatcherServlet 的初始化参数；
     * （2）作为 Web 应用的上下文参数；
     * （3）作为 JNDI 条目；
     * （4）作为环境变量；
     * （5）作为 JVM 的系统属性；
     * （6）在集成测试类上，使用 @ActiveProfiles 注解设置。
     *
     * 你尽可以选择 spring.profiles.active 和 spring.profiles.default 的最佳组合方式以满足需求。
     *
     * 这里推荐使用 DispatcherServlet 的参数将 spring.profiles.default 设置为开发环境的 profile，
     * 即 在 Servlet 上下文中进行设置（为了兼顾到 ContextLoaderListener）。例如，在 Web 应用中，设置
     * spring.profiles.default 的 web.xml 文件会如下所示：
     *
     * <!-- 为上下文设置默认的 profile -->
     * <context-param>
     *     <param-name>spring.profiles.default</param-name>
     *     <param-value>dev</param-value>
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
     *     <!-- 为 Servlet 设置默认的 profile -->
     *     <init-param>
     *         <param-name>spring.profiles.default</param-name>
     *         <param-value>dev</param-value>
     *     </init-param>
     *     <load-on-startup>1</load-on-startup>
     * </servlet>
     *
     * <servlet-mapping>
     *     <servlet-name>appServlet</servlet-name>
     *     <url-pattern>/</url-pattern>
     * </servlet-mapping>
     *
     * 按照这种方式设置 spring.profiles.default，所有的开发人员都能从版本控制软件中获得应用程序源码，并
     * 使用开发环境的设置（如嵌入式数据库）运行代码，而不需要任何额外的配置。
     *
     * 当应用程序部署到 QA、生产或其他环境之中时，负责部署的人根据情况使用系统属性、环境变量或 JNDI 设置
     * spring.profiles.active 即可。当设置 spring.profiles.active 以后，至于 spring.profiles.default
     * 置成什么值就已经无所谓了，系统会优先使用 spring.profiles.active 中所设置的 profile。
     *
     * 不难发现，在 spring.profiles.active 和 spring.profiles.default 中，profile 使用的都是复数
     * 形式。这意味着你可以同时激活多个profile，这可以通过列出多个 profile 名称，并以逗号分隔来实现。当然，
     * 同时启用 dev 和 prod profile 可能也没有太大的意义，不过你可以同时设置多个彼此不相关的 profile。
     *
     *
     *
     * 使用 profile 进行测试
     *
     * 当运行集成测试时，通常会希望采用与生产环境（或者是生产环境的部分子集）相同的配置进行测试。但是，如果
     * 配置中的 bean 定义在了 profile 中，那么在运行测试时，就需要有一种方式来启用合适的 profile。
     *
     * Spring提供了 @ActiveProfiles 注解，可以使用它来指定运行测试时要激活哪个 profile。在集成测试时，
     * 通常想要激活的是开发环境的 profile。例如，下面的测试类片段展现了使用 @ActiveProfiles 激活 dev
     * profile：
     *
     * @RunWith(SpringJUnit4ClassRunner.class)
     * @ContextConfiguration(classes = DataSourceConfig.class)
     * @ActiveProfiles("dev")
     * public class DataSourceConfigTest {
     *
     * }
     *
     * 在条件化创建 bean 方面，Spring 的 profile 机制是一种很棒的方法，这里的条件要基于哪个 profile 处
     * 于激活状态来判断。Spring 4.0 中提供了一种更为通用的机制来实现条件化的 bean 定义，在这种机制之中，
     * 条件完全由你来确定。后续将会介绍如何使用 Spring 4 和 @Conditional 注解定义条件化的 bean。
     */
    public static void main(String[] args) {

    }

}
