package com.siwuxie095.spring.chapter6th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-01-24 20:58:19
 */
public class Main {

    /**
     * 理解视图解析
     *
     * 之前所编写的控制器方法都没有直接产生浏览器中渲染所需的 HTML。这些方法只是将一些数据填充到模型中，然后将模型传递给一个用来
     * 渲染的视图。这些方法会返回一个 String 类型的值，这个值是视图的逻辑名称，不会直接引用具体的视图实现。尽管也编写了几个简单
     * 的 JavaServer Page（JSP）视图，但是控制器并不关心这些。
     *
     * 将控制器中请求处理的逻辑和视图中的渲染实现解耦是 Spring MVC 的一个重要特性。如果控制器中的方法直接负责产生 HTML 的话，
     * 就很难在不影响请求处理逻辑的前提下，维护和更新视图。控制器方法和视图的实现会在模型内容上达成一致，这是两者的最大关联，除
     * 此之外，两者应该保持足够的距离。
     *
     * 但是，如果控制器只通过逻辑视图名来了解视图的话，那 Spring 该如何确定使用哪一个视图实现来渲染模型呢？这就是 Spring 视图
     * 解析器的任务了。
     *
     * 之前使用名为 InternalResourceViewResolver 的视图解析器。在它的配置中，为了得到视图的名字，会使用 "/WEB-INF/views/"
     * 前缀和 ".jsp" 后缀，从而确定来渲染模型的 JSP 文件的物理位置。现在，回过头来看一下视图解析的基础知识以及 Spring 提供的
     * 其他视图解析器。
     *
     * Spring MVC 定义了一个名为 ViewResolver 的接口，它大致如下所示：
     *
     * public interface ViewResolver {
     *
     * 	View resolveViewName(String viewName, Locale locale) throws Exception;
     *
     * }
     *
     * 当给 resolveViewName() 方法传入一个视图名和 Locale 对象时，它会返回一个 View 实例。View 是另外一个接口，如下所示：
     *
     * public interface View {
     *
     * 	String getContentType();
     *
     * 	void render(Map<String, ?> model,
     * 	            HttpServletRequest request,
     * 	            HttpServletResponse response) throws Exception;
     *
     * }
     *
     * View 接口的任务就是接受模型以及 Servlet 的 request 和 response 对象，并将输出结果渲染到 response 中。
     *
     * 这看起来非常简单。所需要做的就是编写 ViewResolver 和 View 的实现，将要渲染的内容放到 response 中，进而展现到用户的浏
     * 览器中。
     *
     * 实际上，并不需要这么麻烦。尽管可以编写 ViewResolver 和 View 的实现，在有些特定的场景下，这样做也是有必要的，但是一般来
     * 讲，并不需要关心这些接口。在这里提及这些接口只是为了让你对视图解析内部如何工作有所了解。Spring 提供了多个内置的实现，它
     * 们能够适应大多数的场景。
     *
     * Spring 自带了 13 个视图解析器，能够将逻辑视图名转换为物理实现：
     * （1）BeanNameViewResolver：将视图解析为 Spring 应用上下文中的 bean，其中 bean 的 ID 与视图的名字相同；
     * （2）ContentNegotiatingViewResolver：通过考虑客户端需要的内容类型来解析视图，委托给另外一个能够产生对应内容类型的视
     * 图解析器；
     * （3）FreeMarkerViewResolver：将视图解析为 FreeMarker 模板；
     * （4）InternalResourceViewResolver：将视图解析为 Web 应用的内部资源（一般为 JSP）；
     * （5）JasperReportsViewResolver：将视图解析为 JasperReports 定义；
     * （6）ResourceBundleViewResolver：将视图解析为资源 bundle（一般为属性文件）；
     * （7）TilesViewResolver：将视图解析为 Apache Tile 定义，其中 tile ID 与视图名称相同。注意有两个不同的 TilesViewResolver
     * 实现，分别对应于 Tiles 2.0 和 Tiles 3.0（这里有两个）；
     * （8）UrlBasedViewResolver：直接根据视图的名称解析视图，视图的名称会匹配一个物理视图的定义；
     * （9）VelocityLayoutViewResolver：将视图解析为 Velocity 布局，从不同的 Velocity 模板中组合页面；
     * （10）VelocityViewResolver：将视图解析为 Velocity 模板；
     * （11）XmlViewResolver：将视图解析为特定 XML 文件中的 bean 定义。类似于 BeanNameViewResolver；
     * （12）XsltViewResolver：将视图解析为 XSLT 转换后的结果。
     *
     * Spring 4 和 Spring 3.2 支持上面的所有视图解析器。Spring 3.1 支持除 Tiles 3 TilesViewResolver 之外的所有视图解析器。
     *
     * 这里没有足够的篇幅介绍 Spring 所提供的 13 种视图解析器。这其实也没什么，因为在大多数应用中，只会用到其中很少的一部分。
     *
     * 对大部分视图解析器来讲，每一项都对应 Java Web 应用中特定的某种视图技术。InternalResourceViewResolver 一般会用于 JSP，
     * TilesViewResolver 用于 Apache Tiles 视图，而 FreeMarkerViewResolver 和 VelocityViewResolver 分别对应 FreeMarker
     * 和 Velocity 模板视图。
     *
     * 在这里，将会关注与大多数 Java 开发人员最息息相关的视图技术。因为大多数 Java Web 应用都会用到 JSP，首先将会介绍
     * InternalResourceViewResolver，这个视图解析器一般会用来解析 JSP 视图。接下来，将会介绍 TilesViewResolver，控制 JSP
     * 页面的布局。
     *
     * 最后，将会看一个没有列在上面的视图解析器。Thymeleaf 是一种用来替代 JSP 的新兴技术，Spring 提供了与 Thymeleaf 的原生模板
     * （natural template）协作的视图解析器，这种模板之所以得到这样的称呼是因为它更像是最终产生的 HTML，而不是驱动它们的 Java
     * 代码。Thymeleaf 是一种非常令人兴奋的视图方案。
     *
     * PS：JSP 曾经是，而且现在依然还是 Java 领域占主导地位的视图技术。
     */
    public static void main(String[] args) {

    }

}
