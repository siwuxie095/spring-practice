package com.siwuxie095.spring.chapter15th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-03-12 08:25:09
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Spring 的 HttpInvoker
     *
     * Spring 开发团队意识到 RMI 服务和基于 HTTP 的服务（例如 Hessian 和 Burlap）之间的空白。一方面，RMI 使用
     * Java 标准的对象序列化机制，但是很难穿透防火墙。另一方面，Hessian 和 Burlap 能很好地穿透防火墙，但是使用私
     * 有的对象序列化机制。
     *
     * 就这样，Spring 的 HTTP invoker 应运而生了。HTTP invoker 是一个新的远程调用模型，作为 Spring 框架的一部
     * 分，能够执行基于 HTTP 的远程调用（让防火墙不为难），并使用 Java 的序列化机制（让开发者也乐观其变）。使用基于
     * HTTP invoker 的服务和使用基于 Hessian/Burlap 的服务非常相似。
     *
     * 为了开始学习 HTTP invoker，下面再来看一下 Spitter 服务 —— 这一次将作为 HTTP invoker 服务来实现。
     *
     *
     *
     * 1、将 bean 导出为 HTTP 服务
     *
     * 要将 bean 导出为 RMI 服务，需要使用 RmiServiceExporter；要将 bean 导出为 Hessian 服务，需要使用
     * HessianServiceExporter；要将 bean 导出为 Burlap 服务，需要使用 BurlapServiceExporter。把这种
     * 千篇一律的用法带到 HTTP invoker 上，应该也不会有任何意外的事情发生，那就是导出 HTTP invoker 服务，
     * 需要使用 HttpInvokerServiceExporter。
     *
     * 为了把 Spitter 服务导出为一个基于 HTTP invoker 的服务，需要像下面的配置一样声明一个
     * HttpInvokerServiceExporter bean：
     *
     *     @Bean
     *     public HttpInvokerServiceExporter httpExporterSpitterService(SpitterService service) {
     *         HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
     *         exporter.setService(service);
     *         exporter.setServiceInterface(SpitterService.class);
     *         return exporter;
     *     }
     *
     * 是否有点似曾相识的感觉？很难找出这个 bean 的定义和在 RMI、Hessian、Burlap 中所声明的 bean 有什么不同。
     * 唯一的区别在于类名：HttpInvokerServiceExporter。否则的话，这个导出器和其他的远程服务的导出器就没有任何
     * 区别了。
     *
     * HttpInvokerServiceExporter 的工作方式与 HessianServiceExporter 和 BurlapServiceExporter 很相似。
     * HttpInvokerServiceExporter 也是一个 Spring 的 MVC 控制器，它通过 DispatcherServlet 接收来自于客户
     * 端的请求，并将这些请求转换成对实现服务的 POJO 的方法调用。
     *
     * PS：HttpInvokerServiceExporter 工作方式与 Hessian 和 Burlap 很相似，通过 Spring MVC 的
     * DispatcherServlet 接收请求，并将这些请求转换成对 Spring bean 的方法调用。
     *
     * 因为 HttpInvokerServiceExporter 是一个 Spring MVC 控制器，需要建立一个 URL 处理器，映射 HTTP URL
     * 到对应的服务上，就像 Hessian 和 Burlap 导出器所做的一样：
     *
     *     @Bean
     *     public HandlerMapping httpInvokerMapping() {
     *         SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
     *         Properties mappings = new Properties();
     *         mappings.setProperty("/spitter.service", "httpExportSpitterService");
     *         mapping.setMappings(mappings);
     *         return mapping;
     *     }
     *
     * 同样，这里需要确保匹配了 DispatcherServlet，这样才能处理对 "*.service" 扩展的请求。
     *
     * PS：DispatcherServlet 的配置，可参考 example4th。
     *
     * 已经知道如何访问由 RMI、Hessian 或 Burlap 所创建的远程服务，现在再次让 Spitter 客户端使用刚才所导出的
     * 基于 HTTP invoker 的服务。
     *
     *
     *
     * 2、通过 HTTP 访问服务
     *
     * 访问基于 HTTP invoker 的服务很类似于之前使用的其他远程服务代理。实际上就是一样的。
     *
     * PS：HttpInvokerProxyFactoryBean 是一个代理工厂 bean，用于生成一个代理，该代理使用 Spring 特有的基于
     * HTTP 协议进行远程通信。
     *
     * 为了把基于 HTTP invoker 的远程服务装配进客户端 Spring 应用上下文中，必须将 HttpInvokerProxyFactoryBean
     * 配置为一个 bean 来代理它，如下所示：
     *
     *     @Bean
     *     public HttpInvokerProxyFactoryBean spitterService() {
     *         HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
     *         proxy.setServiceUrl("http://localhost:8080/Spitter/spitter.service");
     *         proxy.setServiceInterface(SpitterService.class);
     *         return proxy;
     *     }
     *
     * 与 RMI、Hessian、Burlap 中的 bean 定义相对比，会发现几乎没什么变化。serviceInterface 属性仍然用来标识
     * Spitter 服务所实现的接口，而 serviceUrl 属性仍然用来标识远程服务的位置。因为 HTTP invoker 是基于 HTTP
     * 的，如同 Hessian 和 Burlap 一样，serviceUrl 可以包含与 Hessian 和 Burlap 版本中的 bean 一样的 URL。
     *
     * Spring 的 HTTP invoker 是作为两全其美的远程调用解决方案而出现的，把 HTTP 的简单性和 Java 内置的对象序列
     * 化机制融合在一起。这使得 HTTP invoker 服务成为一个引人注目的替代 RMI 或 Hessian/Burlap 的可选方案。
     *
     * 要记住 HTTP invoker 有一个重大的限制：它只是一个 Spring 框架所提供的远程调用解决方案。这意味着客户端和服务
     * 端必须都是 Spring 应用。并且，至少目前而言，也隐含表明客户端和服务端必须是基于 Java 的。另外，因为使用了
     * Java 的序列化机制，客户端和服务端必须使用相同版本的类（与 RMI 类似）。
     *
     * RMI、Hessian、Burlap 和 HTTP invoker 都是远程调用的可选解决方案。但是当面临无所不在的远程调用时，Web 服务
     * 是势不可挡的。后续将介绍 Spring 如何对基于 SOAP 的 Web 服务远程调用提供支持。
     */
    public static void main(String[] args) {

    }

}
