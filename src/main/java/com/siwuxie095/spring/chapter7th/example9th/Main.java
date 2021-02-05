package com.siwuxie095.spring.chapter7th.example9th;

/**
 * @author Jiajing Li
 * @date 2021-02-05 21:10:39
 */
public class Main {

    /**
     * 小结
     *
     * 在 Spring 中，总是会有 "还没有结束" 的感觉：更多的特性、更多的选择以及实现开发目标的更多方式。
     * Spring MVC 有很多功能和技巧。
     *
     * 当然，Spring MVC 的环境搭建是有多种可选方案的一个领域。
     *
     * 在这里，首先看了一下搭建 Spring MVC 中 DispatcherServlet 和 ContextLoaderListener 的
     * 多种方式。还看到了如何调整 DispatcherServlet 的注册功能以及如何注册自定义的 Servlet 和
     * Filter。如果你需要将应用部署到更老的应用服务器上，还快速了解了如何使用 web.xml 声明
     * DispatcherServlet 和 ContextLoaderListener。
     *
     * 然后，了解了如何处理 Spring MVC 控制器所抛出的异常。尽管带有 @RequestMapping 注解的方法
     * 可以在自身的代码中处理异常，但是如果将异常处理的代码抽取到单独的方法中，那么控制器的代码会整
     * 洁得多。
     *
     * 为了采用一致的方式处理通用的任务，包括在应用的所有控制器中处理异常，Spring 3.2 引入了
     * 注解 @ControllerAdvice，它所创建的类能够将控制器的通用行为抽取到同一个地方。
     *
     * 最后，看了一下如何跨重定向传递数据，包括 Spring 对 flash 属性的支持：类似于模型的属性，但是
     * 能在重定向后存活下来。这样的话，就能采用非常恰当的方式为 POST 请求执行一个重定向回应，而且能
     * 够将处理 POST 请求时的模型数据传递过来，然后在重定向后使用或展现这些模型数据。
     */
    public static void main(String[] args) {

    }

}
