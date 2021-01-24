package com.siwuxie095.spring.chapter6th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-01-24 21:50:35
 */
public class Main {

    /**
     * 创建 JSP 视图
     *
     * 不管你是否相信，JavaServer Pages 作为 Java Web 应用程序的视图技术已经超过 15 年了。尽管开始的时候
     * 它很丑陋，只是类似模板技术（如 Microsoft 的 Active Server Pages）的 Java 版本，但 JSP 这些年在
     * 不断进化，包含了对表达式语言和自定义标签库的支持。
     *
     * Spring 提供了两种支持 JSP 视图的方式：
     * （1）InternalResourceViewResolver 会将视图名解析为 JSP 文件。另外，如果在你的 JSP 页面中使用了
     * JSP 标准标签库（JavaServer Pages Standard Tag Library，JSTL）的话，InternalResourceViewResolver
     * 能够将视图名解析为 JstlView 形式的 JSP 文件，从而将 JSTL 本地化和资源 bundle 变量暴露给 JSTL 的
     * 格式化（formatting）和信息（message）标签。
     * （2）Spring 提供了两个 JSP 标签库，一个用于表单到模型的绑定，另一个提供了通用的工具类特性。
     *
     * 不管你使用 JSTL，还是准备使用 Spring 的 JSP 标签库，配置解析 JSP 的视图解析器都是非常重要的。尽管
     * Spring 还有其他的几个视图解析器都能将视图名映射为 JSP 文件，但就这项任务来讲，InternalResourceViewResolver
     * 是最简单和最常用的视图解析器。
     */
    public static void main(String[] args) {

    }

}
