package com.siwuxie095.spring.chapter6th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-01-29 08:24:54
 */
public class Main {

    /**
     * 小结
     *
     * 处理请求只是 Spring MVC 功能的一部分。如果控制器所产生的结果想要让人看到，那么它们产生的模型数据就要渲染到视图中，并展现
     * 到用户的 Web 浏览器中。Spring 的视图渲染是很灵活的，并提供了多个内置的可选方案，包括传统的 JavaServer Pages以及流行的
     * Apache Tiles 布局引擎。
     *
     * 在这里，首先快速了解了一下 Spring 所提供的视图和视图解析可选方案。还深入学习了如何在 Spring MVC 中使用 JSP 和 Apache
     * Tiles。
     *
     * 还看到了如何使用 Thymeleaf 作为 Spring MVC 应用的视图层，它被视为 JSP 的替代方案。Thymeleaf 是一项很有吸引力的技术，
     * 因为它能创建原始的模板，这些模板是纯 HTML，能像静态 HTML 那样以原始的方式编写和预览，并且能够在运行时渲染动态模型数据。
     * 除此之外，Thymeleaf 是与 Servlet 没有耦合关系的，这样它就能够用在 JSP 所不能使用的领域中。
     *
     * Spittr 应用的视图定义完成之后，已经具有了一个虽然微小但是可部署且具有一定功能的 Spring MVC Web 应用。还有一些其他的特性
     * 需要更新进来，如数据持久化和安全性，后续会在合适的时候关注这些特性。
     */
    public static void main(String[] args) {

    }

}
