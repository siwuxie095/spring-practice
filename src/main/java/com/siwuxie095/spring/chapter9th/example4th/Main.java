package com.siwuxie095.spring.chapter9th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-02-14 10:01:34
 */
public class Main {

    /**
     * 过滤 Web 请求
     *
     * Spring Security 借助一系列 Servlet Filter 来提供各种安全性功能。你可能会想，这是否意味着需要在 web.xml
     * 或 WebApplicationInitializer 中配置多个 Filter 呢？实际上，借助于 Spring 的小技巧，只需配置一个 Filter
     * 就可以了。
     *
     * DelegatingFilterProxy 是一个特殊的 Servlet Filter，它本身所做的工作并不多。只是将工作委托给一个 javax.
     * servlet.Filter 实现类，这个实现类作为一个 <bean> 注册在 Spring 应用的上下文中。
     *
     * PS：DelegatingFilterProxy 把 Filter 的处理逻辑委托给 Spring 应用上下文中所定义的一个代理 Filter bean。
     *
     * 如果你喜欢在传统的 web.xml 中配置 Servlet 和 Filter 的话，可以使用 <filter> 元素，如下：
     *
     * <filter>
     *     <filter-name>springSecurityFilterChain</filter-name>
     *     <filter-class>
     *         org.springframework.web.filter.DelegatingFilterProxy
     *     </filter-class>
     * </filter>
     *
     * 在这里，最重要的是 <filter-name> 设置成了 springSecurityFilterChain。这是因为马上就会将 Spring Security
     * 配置在 Web 安全性之中，这里会有一个名为 springSecurityFilterChain 的 Filter bean，DelegatingFilterProxy
     * 会将过滤逻辑委托给它。
     *
     * 如果你希望借助 WebApplicationInitializer 以 Java 的方式来配置 Delegating-FilterProxy 的话，那么所需要
     * 做的就是创建一个扩展的新类：
     *
     * public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {}
     *
     * AbstractSecurityWebApplicationInitializer 实现了 WebApplicationInitializer，因此 Spring 会发现它，
     * 并用它在 Web 容器中注册 DelegatingFilterProxy。尽管可以重载它的 appendFilters() 或 insertFilters()
     * 方法来注册自己选择的 Filter，但是要注册 DelegatingFilterProxy 的话，并不需要重载任何方法。
     *
     * 不管通过 web.xml 还是通过 AbstractSecurityWebApplicationInitializer 的子类来配置 DelegatingFilterProxy，
     * 它都会拦截发往应用中的请求，并将请求委托给 ID 为 springSecurityFilterChain bean。
     *
     * springSecurityFilterChain 本身是另一个特殊的 Filter，它也被称为 FilterChainProxy。它可以链接任意一个或
     * 多个其他的 Filter。Spring Security 依赖一系列 Servlet Filter 来提供不同的安全特性。但是，你几乎不需要知道
     * 这些细节，因为你不需要显式声明 springSecurityFilterChain 以及它所链接在一起的其他 Filter。当启用 Web 安全
     * 性的时候，会自动创建这些 Filter。
     */
    public static void main(String[] args) {

    }

}
