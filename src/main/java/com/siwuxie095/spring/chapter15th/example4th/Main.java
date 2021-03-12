package com.siwuxie095.spring.chapter15th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-03-10 22:19:17
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Hessian 和 Burlap 发布远程服务
     *
     * Hessian 和 Burlap 是 Caucho Technology 提供的两种基于 HTTP 的轻量级远程服务解决方案。借助于尽可能简单的
     * API 和通信协议，它们都致力于简化 Web 服务。
     *
     * 你可能会好奇，为什么 Caucho 对同一个问题会有两种解决方案。Hessian 和 Burlap 就如同一个事物的两面，但是每一
     * 个解决方案都服务于略微不同的目的。
     *
     * Hessian，像 RMI 一样，使用二进制消息进行客户端和服务端的交互。但与其他二进制远程调用技术（例如 RMI）不同的是，
     * 它的二进制消息可以移植到其他非 Java 的语言中，包括 PHP、Python、C++ 和 C#。
     *
     * Burlap 是一种基于 XML 的远程调用技术，这使得它可以自然而然地移植到任何能够解析 XML 的语言上。正因为它基于 XML，
     * 所以相比起 Hessian 的二进制格式而言，Burlap 可读性更强。但是和其他基于 XML 的远程技术（例如 SOAP 或 XML-RPC）
     * 不同，Burlap 的消息结构尽可能的简单，不需要额外的外部定义语言（例如 WSDL 或 IDL）。
     *
     * 你可能想知道如何在 Hessian 和 Burlap 之间做出选择。很大程度上，它们是一样的。唯一的区别在于 Hessian 的消息
     * 是二进制的，而 Burlap 的消息是 XML。由于 Hessian 的消息是二进制的，所以它在带宽上更具优势。但是如果更注重可
     * 读性（如出于调试的目的）或者应用需要与没有 Hessian 实现的语言交互，那么 Burlap 的 XML 消息会是更好的选择。
     *
     * 为了在 Spring 中演示 Hessian 和 Burlap 服务，这里会参照之前使用 RMI 解决 Spitter 服务的示例。下面看看如
     * 何使用 Hessian 和 Burlap 作为远程调用模型来解决这个问题。
     *
     *
     *
     * 1、使用 Hessian 和 Burlap 导出 bean 的功能
     *
     * 这里希望把 SpitterServiceImpl 类的功能发布为远程服务 —— 一个 Hessian 服务。即使没有 Spring，编写一个
     * Hessian 服务也是相当容易的。只需要编写一个继承 com.caucho.hessian.server.HessianServlet 的类，并确
     * 保所有的服务方法是 public 的（在 Hessian 里，所有 public 方法被视为服务方法）。
     *
     * 因为 Hessian 服务很容易实现，Spring 并没有做更多简化 Hessian 模型的工作。但是和 Spring 一起使用时，
     * Hessian 服务可以在各方面利用 Spring 框架的优势，这是纯 Hessian 服务所不具备的。包括利用 Spring 的
     * AOP 来为 Hessian 服务提供系统级服务，例如声明式事务。
     *
     *
     * 1.1、导出 Hessian 服务
     *
     * 在 Spring 中导出一个 Hessian 服务和在 Spring 中实现一个 RMI 服务惊人的相似。为了把 Spitter 服务 bean
     * 发布为 RMI 服务，需要在 Spring 配置文件中配置一个 RmiServiceExporter bean。同样的方式，为了把 Spitter
     * 服务发布为 Hessian 服务，需要配置另一个导出 bean，只不过这次是 HessianServiceExporter。
     *
     * HessianServiceExporter 对 Hessian 服务所执行的功能与 RmiServiceExporter 对 RMI 服务所执行的功能是
     * 相同的：它把 POJO 的 public 方法发布成 Hessian 服务的方法。不过，其实现过程与 RmiServiceExporter 将
     * POJO 发布为 RMI 服务是不同的。
     *
     * PS：HessianServiceExporter 是一个 Spring MVC 控制器，它可以接收 Hessian 请求，并把这些请求转换成对
     * POJO 的调用从而将 POJO 导出为一个 Hessian 服务。
     *
     * HessianServiceExporter 是一个 Spring MVC 控制器，它接收 Hessian 请求，并将这些请求转换成对被导出 POJO
     * 的方法调用。在如下 Spring 的声明中，HessianServiceExporter 会把 spitterService bean 导出为 Hessian
     * 服务：
     *
     *     @Bean
     *     public HessianServiceExporter hessianExporterSpitterService(SpitterService spitterService) {
     *         HessianServiceExporter exporter = new HessianServiceExporter();
     *         exporter.setService(spitterService);
     *         exporter.setServiceInterface(SpitterService.class);
     *         return exporter;
     *     }
     *
     * 正如 RmiServiceExporter 一样，service 属性的值被设置为实现了这个服务的 bean 引用。在这里，它引用的是
     * spitterService bean。serviceInterface 属性用来标识这个服务实现了 SpitterService 接口。
     *
     * 与 RmiServiceExporter 不同的是，这里不需要设置 serviceName 属性。在 RMI 中，serviceName 属性用来在
     * RMI 注册表中注册一个服务。而 Hessian 没有注册表，因此也就没必要为 Hessian 服务进行命名。
     *
     *
     * 1.2、配置 Hessian 控制器
     *
     * RmiServiceExporter 和 HessianServiceExporter 另外一个主要区别就是，由于 Hessian 是基于 HTTP 的，
     * 所以 HessianSeriviceExporter 实现为一个 Spring MVC 控制器。这意味着为了使用导出的 Hessian 服务，
     * 需要执行两个额外的配置步骤：
     * （1）在 web.xml 中配置 Spring 的 DispatcherServlet，并把应用部署为 Web 应用；
     * （2）在 Spring 的配置文件中配置一个 URL 处理器，把 Hessian 服务的 URL 分发给对应的 Hessian 服务 bean。
     *
     * 之前已经学习了如何配置 Spring 的 DispatcherServlet 和 URL 处理器。首先，需要一个 DispatcherServlet，
     * 其次，为了处理 Hessian 服务，DispatcherServlet 还需要配置一个 Servlet 映射来拦截后缀为 "*.service"
     * 的 URL：
     *
     * <servlet-mapping>
     *     <servlet-name>spitter</servlet-name>
     *     <url-pattern>*.service</url-pattern>
     * </servlet-mapping>
     *
     * 如果你在 Java 中通过实现 WebApplicationInitializer 来配置 DispatcherServlet 的话，那么需要将 URL
     * 模式作为映射添加到 ServletRegistration.Dynamic 中，在将 DispatcherServlet 添加到容器中的时候，能够
     * 得到 ServletRegistration.Dynamic 对象：
     *
     * ServletRegistration.Dynamic dispatcher = container.addServlet(
     *      "appServlet", new DispatcherServlet(dispatcherServletContext));
     * dispatcher.setLoadOnStartup(1);
     * dispatcher.addMapping("/");
     * dispatcher.addMapping("*.service");
     *
     * 或者，如果你通过扩展 AbstractDispatcherServletInitializer 或
     * AbstractAnnotationConfigDispatcherServletInitializer 的方式来配置 DispatcherServlet，那么在重载
     * getServletMappings() 的时候，需要包含该映射：
     *
     * @Override
     * protected String[] getServletMappings() {
     *     return new String[] { "/", "*.service" };
     * }
     *
     * 这样配置后，任何以 ".service" 结束的 URL 请求都将由 DispatcherServlet 处理，它会把请求传递给匹配这个
     * URL 的控制器。因此 "/spitter.service" 的请求最终将被 hessianSpitterService bean 所处理（它实际上
     * 仅仅是一个 SpitterServiceImpl 的代理）。
     *
     * 那是如何知道这个请求会转给 hessianSpitterSevice 处理呢？还需要配置一个 URL 映射来确保 DispatcherServlet
     * 把请求转给 hessianSpitterService。如下的 SimpleUrlHandlerMappingbean 可以做到这一点：
     *
     *     @Bean
     *     public HandlerMapping hessianMapping() {
     *         SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
     *         Properties mappings = new Properties();
     *         mappings.setProperty("/spitter.service", "hessianExportSpitterService");
     *         handlerMapping.setMappings(mappings);
     *         return handlerMapping;
     *     }
     *
     * 如果不喜欢 Hessian 的二进制协议，还可以选择使用 Burlap 基于 XML 的协议。下面看看如何把一个服务导出为
     * Burlap 服务。
     *
     *
     * 1.3、导出 Burlap 服务
     *
     * 从任何方面上看，BurlapServiceExporter 与 HessianServiceExporter 实际上都是相同的，只不过它使用基于
     * XML 的协议而不是二进制协议。下面的 bean 定义展示了如何使用 BurlapServiceExporter 把 Spitter 服务导
     * 出为一个 Burlap 服务：
     *
     *     @Bean
     *     public BurlapServiceExporter burlapExporterSpitterService(SpitterService service) {
     *         BurlapServiceExporter exporter = new BurlapServiceExporter();
     *         exporter.setService(service);
     *         exporter.setServiceInterface(SpitterService.class);
     *         return exporter;
     *     }
     *
     * 可以看到，这个 bean 与使用 Hessian 所对应 bean 的唯一区别在于 bean 的方法和导出类。配置 Burlap 服务和
     * 配置 Hessian 服务是一模一样的，这包括需要准备一个 URL 处理器和一个 DispatcherServlet。
     *
     * 下面看看会话的另一端，如何访问这里使用 Hessian（或 Burlap）所发布的服务。
     *
     *
     *
     * 2、访问 Hessian/Burlap 服务
     *
     * 之前在使用 RmiProxyFactoryBean 访问 Spitter 服务的客户端代码中，完全不知道这个服务是一个 RMI 服务。事
     * 实上，也根本没有任何迹象表明这个服务是一个远程服务。它只是与 SpitterService 接口打交道 —— RMI 的所有细
     * 节完全包含在 Spring 配置中的这个 bean 的配置中。好处是客户端不需要了解服务的实现，因此从 RMI 客户端转到
     * Hessian 客户端会变得极其简单，不需要改变任何客户端的 Java 代码。
     *
     * 坏处是，如果你真的喜欢编写 Java 代码的话，那么这里或许让你 "大失所望"。这是因为在客户端代码中，基于 RMI
     * 的服务与基于 Hessian 的服务之间唯一的差别在于要使用 Spring 的 HessianProxyFactoryBean 来代替
     * RmiProxyFactoryBean。客户端调用基于 Hessian 的 Spitter 服务可以用如下的配置声明：
     *
     *     @Bean
     *     public HessianProxyFactoryBean spitterService() {
     *         HessianProxyFactoryBean proxy = new HessianProxyFactoryBean();
     *         proxy.setServiceUrl("http://localhost:8080/Spitter/spitter.service");
     *         proxy.setServiceInterface(SpitterService.class);
     *         return proxy;
     *     }
     *
     * 就像基于 RMI 服务那样，serviceInterface 属性指定了这个服务实现的接口。并且，像 RmiProxyFactoryBean
     * 一样，serviceUrl 标识了这个服务的 URL。既然 Hessian 是基于 HTTP 的，当然在这里要设置一个 HTTP URL
     * （URL是由先前定义的URL映射所决定的）。
     *
     * PS：HessianProxyFactoryBean 和 BurlapProxyFactoryBean 生成的代理对象负责通过 HTTP（Hessian 为
     * 二进制、Burlap为XML）与远程对象通信。
     *
     * 事实证明，把 Burlap 服务装配进客户端同样也没有太多新意。二者唯一的区别在于，要使用
     * BurlapProxyFactoryBean 来代替 HessianProxyFactoryBean：
     *
     *     @Bean
     *     public BurlapProxyFactoryBean spitterService() {
     *         BurlapProxyFactoryBean proxy = new BurlapProxyFactoryBean();
     *         proxy.setServiceUrl("http://localhost:8080/Spitter/spitter.service");
     *         proxy.setServiceInterface(SpitterService.class);
     *         return proxy;
     *     }
     *
     * 尽管觉得在 RMI、Hessian 和 Burlap 服务之间稍微不同的配置是很无趣的，但是这样的单调恰恰是有好处的。它
     * 意味着可以很容易在各种 Spring 所支持的远程调用技术之间进行切换，而不需要重新学习一个全新的模型。一旦配
     * 置了对 RMI 服务的引用，把它重新配置为 Hessian 或 Burlap 服务也是很轻松的工作。
     *
     * 因为 Hessian 和 Burlap 都是基于 HTTP 的，它们都解决了 RMI 所头疼的防火墙渗透问题。但是当传递过来的
     * RPC 消息中包含序列化对象时，RMI 就完胜 Hessian 和 Burlap 了。因为 Hessian 和 Burlap 都采用了私有
     * 的序列化机制，而 RMI 使用的是 Java 本身的序列化机制。如果数据模型非常复杂，Hessian/Burlap 的序列化
     * 模型就可能无法胜任了。
     *
     * 其实还有一个两全其美的解决方案后续将会介绍 Spring 的 HTTP invoker，它基于 HTTP 提供了 RPC
     * （像 Hessian/Burlap一样），同时又使用了 Java 的对象序列化机制（像 RMI 一样）。
     */
    public static void main(String[] args) {

    }

}
