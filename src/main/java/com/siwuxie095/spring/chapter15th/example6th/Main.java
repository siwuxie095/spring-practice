package com.siwuxie095.spring.chapter15th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-03-14 21:31:40
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 发布和使用 Web 服务
     *
     * 近几年，最流行的一个 TLA（三个字母缩写）就是 SOA（面向服务的架构）。SOA 对不同的人意味着不同的意义。但是，SOA
     * 的核心理念是，应用程序可以并且应该被设计成依赖于一组公共的核心服务，而不是为每个应用都重新实现相同的功能。
     *
     * 例如，一个金融机构可能有若干个应用，其中很多都需要访问借款者的账户信息。在这种情况下，应用应该都依赖于一个公共的
     * 获取账户信息的服务，而不应该在每一个应用中都建立账户访问逻辑（其中大部分逻辑都是重复的）。
     *
     * Java 与 Web 服务的结合已经有很长的历史了，而且在 Java 中使用 Web 服务有多种选择。其中的大多数可选方案已经以某
     * 种方式与 Spring 进行了整合。虽然 Spring 为使用 Java API for XML Web Service（JAX-WS）来发布和使用 SOAP
     * Web 服务提供了大力支持，但是在这里不可能涵盖每一个 Spring 所支持的 Web 服务框架和工具箱。
     *
     * 在本节，将重新回顾下 Spitter 服务示例，不过这次将使用 Spring 对 JAX-WS 的支持来把 Spitter 服务发布为 Web
     * 服务并使用此 Web 服务。首先，来看一下如何在 Spring 中创建 JAX-WS Web 服务。
     *
     *
     *
     * 1、创建基于 Spring 的 JAX-WS 端点
     *
     * 之前使用了 Spring 的服务导出器创建了远程服务。这些服务导出器很神奇地将 Spring 配置的 POJO 转换成了远程服务。
     * 看到了如何使用 RmiServiceExporter 创建 RMI 服务，如何使用 HessianServiceExporter 创建 Hessian 服务，
     * 如何使用 BurlapServiceExporter 创建 Burlap 服务，以及如何使用 HttpInvokerServiceExporter 创建HTTP
     * invoker 服务。现在你或许期望这里会展示如何使用一个JAX-WS 服务导出器创建 Web 服务。
     *
     * Spring 的确提供了一个 JAX-WS 服务导出器，SimpleJaxWsServiceExporter，很快就可以看到。但在这之前，你必须
     * 知道它并不一定是所有场景下的最好选择。SimpleJaxWsServiceExporter 要求 JAX-WS 运行时支持将端点发布到指定
     * 地址上。Sun JDK 1.6 自带的 JAX-WS 可以符合要求，但是其他的 JAX-WS 实现，包括 JAX-WS 的参考实现，可能并不
     * 能满足此需求。
     *
     * 如果将要部署的 JAX-WS 运行时不支持将其发布到指定地址上，那就要以更为传统的方式来编写 JAX-WS 端点。这意味着端点
     * 的生命周期由 JAX-WS 运行时来进行管理，而不是 Spring。但是这并不意味着它们不能装配 Spring 上下文中的 bean。
     *
     *
     * 1.1、在 Spring 中自动装配 JAX-WS 端点
     *
     * JAX-WS 编程模型使用注解将类和类的方法声明为 Web 服务的操作。使用 @WebService 注解所标注的类被认为 Web 服务
     * 的端点，而使用 @WebMethod 注解所标注的方法被认为是操作。
     *
     * 就像大规模应用中的其他对象一样，JAX-WS 端点很可能需要与其他对象交互来完成工作。这意味着 JAX-WS 端点可以受益于
     * 依赖注入。但是如果端点的生命周期由 JAX-WS 运行时来管理，而不是由 Spring 来管理的话，这似乎不可能把 Spring
     * 管理的 bean 装配进 JAX-WS 管理的端点实例中。
     *
     * 装配JAX-WS端点的秘密在于继承 SpringBeanAutowiringSupport。通过继承 SpringBeanAutowiringSupport，可以
     * 使用 @Autowired 注解标注端点的属性，依赖就会自动注入了。SpitterServiceEndpoint 展示了它是如何工作的。
     *
     * @WebService(serviceName = "SpitterService")
     * public class SpitterServiceEndpoint extends SpringBeanAutowiringSupport {
     *
     *     @Autowired
     *     private SpitterService spitterService;
     *
     *     @WebMethod
     *     public void addSpittle(Spittle spittle) {
     *         spitterService.saveSpittle(spittle);
     *     }
     *
     *     @WebMethod
     *     public void deleteSpittle(long spittleId) {
     *         spitterService.deleteSpittle(spittleId);
     *     }
     *
     *     @WebMethod
     *     public List<Spittle> getRecentSpittles(int spittleCount) {
     *         return spitterService.getRecentSpittles(spittleCount);
     *     }
     *
     *     @WebMethod
     *     public List<Spittle> getSpittlesForSpitter(Spitter spitter) {
     *         return spitterService.getSpittlesForSpitter(spitter);
     *     }
     *
     * }
     *
     * 这里在 SpitterService 属性上使用 @Autowired 注解来表明它应该自动注入一个从 Spring 应用上下文中所获取的
     * bean。在这里，端点委托注入的 SpitterService 来完成实际的工作。
     *
     *
     * 1.2、导出独立的 JAX-WS 端点
     *
     * 当对象的生命周期不是由 Spring 管理的，而对象的属性又需要注入 Spring 所管理的 bean 时，
     * SpringBeanAutowiringSupport 很有用。在合适场景下，还是可以把 Spring 管理的 bean
     * 导出为 JAX-WS 端点的。
     *
     * Spring SimpleJaxWsServiceExporter 的工作方式很类似于之前所介绍的其他服务导出器。它把 Spring 管理的
     * bean 发布为 JAX-WS 运行时中的服务端点。与其他服务导出器不同，SimpleJaxWsServiceExporter 不需要为它
     * 指定一个被导出 bean 的引用，它会将使用 JAX-WS 注解所标注的所有 bean 发布为 JAX-WS 服务。
     *
     *     @Bean
     *     public SimpleJaxWsServiceExporter jaxwsExporter() {
     *         return new SimpleJaxWsServiceExporter();
     *     }
     *
     * 正如这里所看到的，SimpleJaxWsServiceExporter 不需要再做其他的事情就可以完成所有的工作。当启动的时候，
     * 它会搜索 Spring 应用上下文来查找所有使用 @WebService 注解的 bean。当找到符合的 bean 时，
     * SimpleJaxWsServiceExporter 使用 http://localhost:8080/ 地址将 bean 发布为 JAX-WS 端点。
     * SpitterServiceEndpoint 就是其中一个被查找到的 bean。
     *
     * @Component
     * @WebService(serviceName = "SpitterService")
     * public class SpitterServiceEndpoint {
     *
     *     @Autowired
     *     private SpitterService spitterService;
     *
     *     @WebMethod
     *     public void addSpittle(Spittle spittle) {
     *         spitterService.saveSpittle(spittle);
     *     }
     *
     *     @WebMethod
     *     public void deleteSpittle(long spittleId) {
     *         spitterService.deleteSpittle(spittleId);
     *     }
     *
     *     @WebMethod
     *     public List<Spittle> getRecentSpittles(int spittleCount) {
     *         return spitterService.getRecentSpittles(spittleCount);
     *     }
     *
     *     @WebMethod
     *     public List<Spittle> getSpittlesForSpitter(Spitter spitter) {
     *         return spitterService.getSpittlesForSpitter(spitter);
     *     }
     *
     * }
     *
     * 可以注意到 SpitterServiceEndpoint 的新实现不再继承 SpringBeanAutowiring-Support 了。它完全就是
     * 一个 Spring bean，因此 SpitterServiceEndpoint 不需要继承任何特殊的支持类就可以实现自动装配。
     *
     * 因为 SimpleJaxWsServiceExporter 的默认基本地址为 http://localhost:8080/，而
     * SpitterServiceEndpoint 使用了 @Webservice(servicename="SpitterService")注解，所以这两个 bean
     * 所形成的 Web 服务地址均为 http://localhost:8080/SpitterService。但是完全可以控制服务 URL，如果希望
     * 调整服务 URL 的话，可以调整基本地址。例如，如下 SimpleJaxWsServiceExporter 的配置把相同的服务端点发布
     * 到 http://localhost:8889/srvices/SpitterService。
     *
     *     @Bean
     *     public SimpleJaxWsServiceExporter jaxwsExporter() {
     *         SimpleJaxWsServiceExporter exporter = new SimpleJaxWsServiceExporter();
     *         exporter.setBaseAddress("http://localhost:8889/services/");
     *         return exporter;
     *     }
     *
     * SimpleJaxWsServiceExporter 就像看起来那么简单，但是应该注意它只能用在支持将端点发布到指定地址的
     * JAX-WS 运行时中。这包含了 Sun 1.6 JDK 自带的 JAX-WS 运行时。其他的 JAX-WS 运行时，例如 JAX-WS
     * 2.1 的参考实现，不支持这种类型的端点发布，因此也就不能使用 SimpleJaxWsServiceExporter。
     *
     *
     *
     * 2、在客户端代理 JAX-WS 服务
     *
     * 使用 Spring 发布 Web 服务与使用 RMI、Hessian、Burlap 和 HTTP invoker 发布服务是有所不同的。但是很快
     * 就会发现，借助 Spring 使用 Web 服务所涉及的客户端代理的工作方式与基于 Spring 的客户端使用其他远程调用技
     * 术是相同的。
     *
     * 使用 JaxWsProxyFactoryBean，可以在 Spring 中装配 Spitter Web 服务，与任意一个其他的 bean 一样。
     * JaxWsProxyFactoryBean 是 Spring 工厂 bean，它能生成一个知道如何与 SOAP Web 服务交互的代理。所创建的
     * 代理实现了服务接口。因此，JaxWsProxyFactoryBean 让装配和使用一个远程 Web 服务变成了可能，就像这个远程
     * Web 服务是本地 POJO 一样。
     *
     * PS：JaxWsPortProxyFactoryBean 生成可以与远程 Web 服务交互的代理。这些代理可以被装配到其他 bean 中，
     * 就像它们是本地 POJO 一样。
     *
     * 可以像下面这样配置 JaxWsPortProxyFactoryBean 来引用 Spitter 服务：
     *
     *     @Bean
     *     public JaxWsPortProxyFactoryBean spitterService() throws MalformedURLException {
     *         JaxWsPortProxyFactoryBean proxy = new JaxWsPortProxyFactoryBean();
     *         proxy.setWsdlDocumentUrl(new URL("http://localhost:8889/services/SpitterService?wsdl"));
     *         proxy.setServiceName("spitterService");
     *         proxy.setPortName("spitterServiceHttpPort");
     *         proxy.setServiceInterface(SpitterService.class);
     *         proxy.setNamespaceUri("http://spitter.com");
     *         return proxy;
     *     }
     *
     * 可以看到，为 JaxWsPortProxyFactoryBean 设置几个属性就可以工作了。wsdlDocumentUrl 属性标识了远程 Web
     * 服务定义文件的位置。JaxWsPortProxyFactoryBean 将使用这个位置上可用的 WSDL 来为服务创建代理。由
     * JaxWsPortProxyFactoryBean 所生成的代理实现了 serviceInterface 属性所指定的 SpitterService 接口。
     *
     * 剩下的三个属性的值通常可以通过查看服务的 WSDL 来确定。为了演示，假设 Spitter 服务的 WSDL 如下所示：
     *
     * <wsdl:definitions targetNamespace="http://spitter.com">
     *     <wsdl:service name="spitterService">
     *         <wsdl:port name="spitterServicePort" binding="tns:spitterServiceHttpBinding" />
     *     </wsdl:service>
     * </wsdl:definitions>
     *
     * 虽然不太可能这么做，但是在服务的 WSDL 中定义多个服务和端口是允许的。鉴于此，JaxWsPortProxyFactoryBean
     * 需要使用 portName 和 serviceName 属性指定端口和服务名称。WSDL 中 <wsdl:port> 和 <wsdl:service>
     * 元素的 name 属性可以帮助识别出这些属性该设置成什么。
     *
     * 最后，namespaceUri 属性指定了服务的命名空间。命名空间将有助于 JaxWsPortProxyFactoryBean 去定位 WSDL
     * 中的服务定义。正如端口和服务名一样，可以在 WSDL 中找到该属性的正确值。它通常会在 <wsdl:definitions> 的
     * targetNamespace 属性中。
     */
    public static void main(String[] args) {

    }

}
