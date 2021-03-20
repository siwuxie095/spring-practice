package com.siwuxie095.spring.chapter16th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-03-20 20:40:53
 */
public class Main {

    /**
     * 小结
     *
     * RESTful 架构使用 Web 标准来集成应用程序，使得交互变得简单自然。系统中的资源采用 URL 进行标识，
     * 使用 HTTP 方法进行管理并且会以一种或多种适合客户端的方式来进行表述。
     *
     * 在这里，看到了如何编写响应 RESTful 资源管理请求的 Spring MVC 控制器。借助参数化的 URL 模式
     * 并将控制器处理方法与特定的 HTTP 方法关联，控制器能够响应对资源的 GET、POST、PUT 以及 DELETE
     * 请求。
     *
     * 为了响应这些请求，Spring 能够将资源背后的数据以最适合客户端的形式展现。对于基于视图的响应，
     * ContentNegotiatingViewResolver 能够在多个视图解析器产生的视图中选择出最适合客户端期望内容
     * 类型的那一个。或者，控制器的处理方法可以借助 @ResponseBody 注解完全绕过视图解析，并使用信息
     * 转换器将返回值转换为客户端的响应。
     *
     * REST API 为客户端暴露了应用的功能，它们暴露功能的方式恐怕最原始的 API 设计者做梦都想不到。
     * REST API 的客户端通常是移动应用或运行在 Web 浏览器中的 JavaScript。但是，Spring 应用
     * 也可以借助 RestTemplate 来使用这些 API。
     *
     * REST 只是应用间通信的方法之一，后续将会学习如何在 Spring 应用中借助消息实现异步通信。
     */
    public static void main(String[] args) {

    }

}
