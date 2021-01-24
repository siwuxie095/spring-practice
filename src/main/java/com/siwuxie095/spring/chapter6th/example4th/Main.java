package com.siwuxie095.spring.chapter6th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-01-24 22:12:46
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 配置适用于 JSP 的视图解析器
     *
     * 有一些视图解析器，如 ResourceBundleViewResolver 会直接将逻辑视图名映射为特定的 View 接口实现，而
     * InternalResourceViewResolver 所采取的方式并不那么直接。它遵循一种约定，会在视图名上添加前缀和后缀，
     * 进而确定一个 Web 应用中视图资源的物理路径。
     *
     * 作为样例，考虑一个简单的场景，假设逻辑视图名为 home。通用的实践是将 JSP 文件放到 Web 应用的 WEB-INF
     * 目录下，防止对它的直接访问。如果将所有的 JSP 文件都放在 "/WEB-INF/views/" 目录下，并且 home 页的
     * JSP 名为 home.jsp，那么可以确定物理视图的路径就是逻辑视图名 home 再加上 "/WEB-INF/views/" 前缀和
     * ".jsp" 后缀。
     *
     * 当使用 @Bean 注解的时候，可以按照如下的方式配置 InternalResourceViewResolver，使其在解析视图时，
     * 遵循上述的约定。
     *
     *     @Bean
     *     public ViewResolver viewResolver() {
     *         InternalResourceViewResolver resolver = new InternalResourceViewResolver();
     *         resolver.setPrefix("/WEB-INF/views/");
     *         resolver.setSuffix(".jsp");
     *         return resolver;
     *     }
     *
     * 作为替代方案，如果你更喜欢使用基于 XML 的 Spring 配置，那么可以按照如下的方式配置：
     *
     *     <bean id="viewResolver"
     *           class="org.springframework.web.servlet.view.InternalResourceViewResolver"
     *           p:prefix="/WEB-INF/views/"
     *           p:suffix=".jsp" />
     *
     * InternalResourceViewResolver 配置就绪之后，它就会将逻辑视图名解析为 JSP 文件，如下所示：
     * （1）home 将会解析为 "/WEB-INF/views/home.jsp"；
     * （2）productList 将会解析为 "/WEB-INF/views/productList.jsp"；
     * （3）books/detail 将会解析为 "/WEB-INF/views/books/detail.jsp"。
     *
     * 这里重点看一下最后一个样例。当逻辑视图名中包含斜线时，这个斜线也会带到资源的路径名中。因此，它会对应到
     * prefix 属性所引用目录的子目录下的 JSP 文件。这样的话，就可以很方便地将视图模板组织为层级目录结构，而
     * 不是将它们都放到同一个目录之中。
     *
     *
     *
     * 解析 JSTL 视图
     *
     * 到目前为止，对 InternalResourceViewResolver 的配置都很基础和简单。它最终会将逻辑视图名解析为
     * InternalResourceView 实例，这个实例会引用 JSP 文件。但是如果这些 JSP 使用 JSTL 标签来处理格式化
     * 和信息的话，那么会希望 InternalResourceViewResolver 将视图解析为 JstlView。
     *
     * JSTL 的格式化标签需要一个 Locale 对象，以便于恰当地格式化地域相关的值，如日期和货币。信息标签可以借
     * 助 Spring 的信息资源和 Locale，从而选择适当的信息渲染到 HTML 之中。通过解析 JstlView，JSTL 能够
     * 获得 Locale 对象以及 Spring 中配置的信息资源。
     *
     * 如果想让 InternalResourceViewResolver 将视图解析为 JstlView，而不是 InternalResourceView
     * 的话，那么只需设置它的 viewClass 属性即可：
     *
     *     @Bean
     *     public ViewResolver viewResolver() {
     *         InternalResourceViewResolver resolver = new InternalResourceViewResolver();
     *         resolver.setPrefix("/WEB-INF/views/");
     *         resolver.setSuffix(".jsp");
     *         resolver.setViewClass(JstlView.class);
     *         return resolver;
     *     }
     *
     * 同样，也可以使用 XML 完成这一任务：
     *
     *     <bean id="viewResolver"
     *           class="org.springframework.web.servlet.view.InternalResourceViewResolver"
     *           p:prefix="/WEB-INF/views/"
     *           p:suffix=".jsp"
     *           p:viewClass="org.springframework.web.servlet.view.JstlView" />
     *
     * 不管使用 Java 配置还是使用 XML，都能确保 JSTL 的格式化和信息标签能够获得 Locale 对象以及 Spring
     * 中配置的信息资源。
     */
    public static void main(String[] args) {

    }

}
