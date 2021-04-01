package com.siwuxie095.spring.chapter20th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-04-01 08:01:43
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 远程 MBean
     *
     * 虽然最初的 JMX 规范提及了通过 MBean 进行应用的远程管理，但是它并没有定义实际的远程访问协议或 API。因此，会由
     * JMX 供应商定义自己的 JMX 远程访问解决方案，但这通常又是专有的。
     *
     * 为了满足以标准方式进行远程访问 JMX 的需求，JCP（Java Community Process）制订了 JSR-160：Java 管理扩展远
     * 程访问 API 规范（Java Management Extensions Remote API Specification）。该规范定义了 JMX 远程访问的
     * 标准， 该标准至少需要绑定 RMI 和可选的 JMX 消息协议（JMX Messaging Protocol，JMXMP）。
     *
     * 在这里，将看到 Spring 如何远程访问 MBean。首先从配置 Spring 把 SpittleController 导出为远程 MBean 开始，
     * 然后再了解如何使用 Spring 远程操纵 MBean。
     *
     *
     *
     * 1、暴露远程 MBean
     *
     * 使 MBean 成为远程对象的最简单方式是配置 Spring 的 ConnectorServerFactoryBean：
     *
     *     @Bean
     *     public ConnectorServerFactoryBean connectorServerFactoryBean() {
     *         return new ConnectorServerFactoryBean();
     *     }
     *
     * ConnectorServerFactoryBean 会创建和启动 JSR-160 JMXConnectorServer。默认情况下，服务器使用 JMXMP 协
     * 议并监听端口 9875 —— 因此，它将绑定 "service:jmx:jmxmp://localhost:9875"。但是导出 MBean 的可选方案
     * 并不局限于 JMXMP。
     *
     * 根据不同 JMX 的实现，有多种远程访问协议可供选择，包括远程方法调用（Remote Method Invocation，RMI）、SOAP、
     * Hessian/Burlap 和 IIOP（Internet InterORB Protocol）。为 MBean 绑定不同的远程访问协议，仅需要设置
     * ConnectorServerFactoryBean 的 serviceUrl 属性。例如，如果想使用 RMI 远程访问 MBean，可以像下面示例这样
     * 配置：
     *
     *     @Bean
     *     public ConnectorServerFactoryBean rmiConnectorServerFactoryBean() {
     *         ConnectorServerFactoryBean csfb = new ConnectorServerFactoryBean();
     *         csfb.setServiceUrl(
     *                 "service:jmx:rmi://localhost/jndi/rmi://localhost:9875/spitter");
     *         return csfb;
     *     }
     *
     * 在这里，将 ConnectorServerFactoryBean 绑定到了一个 RMI 注册表，该注册表监听本机的 1099 端口。这意味着需
     * 要一个 RMI 注册表运行时，并监听该端口。RmiServiceExporter 可以自动启动一个 RMI 注册表。但是，在本示例中不
     * 使用 RmiServiceExporter，而是通过在 Spring 中声明 RmiRegistryFactoryBean 来启动一个 RMI 注册表，如下
     * 面的 @Bean 方法所示：
     *
     *     @Bean
     *     public RmiRegistryFactoryBean rmiRegistryFB() {
     *         RmiRegistryFactoryBean rmiRegistryFB = new RmiRegistryFactoryBean();
     *         rmiRegistryFB.setPort(1099);
     *         return rmiRegistryFB;
     *     }
     *
     * 没错！现在 MBean 可以通过 RMI 进行远程访问了。但是如果没有人通过 RMI 访问 MBean 的话，那就不值得这么做。所
     * 以现在把关注点转向 JMX 远程访问的客户端，看看如何在 Spring 中装配一个远程 MBean 到 JMX 客户端中。
     *
     *
     *
     * 2、访问远程 MBean
     *
     * 要想访问远程 MBean 服务器，需要在 Spring 上下文中配置 MbeanServerConnectionFactoryBean。下面的 bean
     * 声明装配了一个 MbeanServerConnectionFactoryBean，该 bean 用于访问上面所创建的基于 RMI 的远程服务器。
     *
     *     @Bean
     *     public MBeanServerConnectionFactoryBean connectionFactoryBean()
     *             throws MalformedURLException {
     *         MBeanServerConnectionFactoryBean mbscfb =
     *                 new MBeanServerConnectionFactoryBean();
     *         mbscfb.setServiceUrl(
     *                 "service:jmx:rmi://localhost/jndi/rmi://localhost:1099/spitter");
     *         return mbscfb;
     *     }
     *
     * 顾名思义，MBeanServerConnectionFactoryBean 是一个可用于创建 MBeanServerConnection 的工厂 bean。由
     * MBeanServerConnectionFactoryBean 所生成的 MBeanServerConnection 实际上是作为远程 MBean 服务器的本
     * 地代理。它能够以 MBeanServerConnection 的形式注入到其他 bean 的属性中：
     *
     *     @Bean
     *     public JmxClient jmxClient(MBeanServerConnection connection) {
     *         JmxClient jmxClient = new JmxClient();
     *         jmxClient.setMBeanServerConnection(connection);
     *         return jmxClient;
     *     }
     *
     * MBeanServerConnection 提供了多种方法，可以使用这些方法查询远程 MBean 服务器并调用 MBean 服务器内所注册
     * 的 MBean 的方法。例如，如果希望知道在远程 MBean 服务器中有多少已注册的 MBean，可以用如下的代码片段打印这
     * 些信息：
     *
     * int mbeanCount = mbeanServerConnection.getMBeanCount();
     * System.out.println("There are " + mbeanCount + " MBeans");
     *
     * 还可以使用 queryNames() 方法查询远程服务器中所有 MBean 的名称：
     *
     * Set<ObjectName> mbeanNames = mbeanServerConnection.queryNames(null, null);
     *
     * 传递给 queryNames() 方法的两个参数用于过滤查询结果。如果将两个参数都设置为 null，输出结果为所有已注册的
     * MBean 的名称。
     *
     * 查询远程 MBean 服务器上 bean 的数量和名称虽然很有趣，不过并不能完成更多的工作。远程访问 MBean 服务器的
     * 真正价值在于访问远程服务器上已注册 MBean 的属性以及调用它们的方法。
     *
     * 为了访问 MBean 属性，可以使用 getAttribute() 和 setAttribute() 方法。例如，为了获取 MBean 属性的值，
     * 可以按照下面的方法调用 getAttribute() 方法：
     *
     * String cronExpression = mbeanServerConnection.getAttribute(
     *      new ObjectName("spitter:name=SpittleController"), "spittlesPerPage");
     *
     * 同样，可以使用 setAttribute() 方法改变 MBean 属性的值：
     *
     * mbeanServerConnection.setAttribute(
     *      new ObjectName("spitter:name=SpittleController"), new Attribute("spittlesPerPage", 10));
     *
     * 如果希望调用 MBean 的操作，那需要使用 invoke() 方法。下面的内容描述了如何调用 SpittleController MBean
     * 的 setSpittlesPerPage() 方法：
     *
     * mbeanServerConnection.invoke(
     *      new ObjectName("spitter:name=SpittleController"),
     *      "setSpittlesPerPage",
     *      new Object[] {100},
     *      new String[] {"int"});
     *
     * 还可以使用 MBeanServerConnection 的方法对远程 MBean 做很多其他的事情。不过，通过 MBeanServerConnection
     * 对远程 MBean 进行方法调用和属性设置是一种很笨拙的方法。要想调用 setSpittlesPerPage() 这样一个简单的方法，
     * 需要创建一个 ObjectName 实例，并向 invoke() 方法传递几个参数。它并不是直观的方法调用。为了更直接地调用方法，
     * 需要代理远程 MBean。
     *
     *
     *
     * 3、代理 MBean
     *
     * Spring 的 MBeanProxyFactoryBean 是一个代理工厂 bean，像之前所演示的远程代理工厂 bean 类似。在之前所介
     * 绍的内容中，它们会提供代理，用来访问远程的 Spring 受管 bean，与之不同，MBcanProxyFactoryBean 可以直接
     * 访问远程的 MBean（就如同配置在本地的其他 bean 一样）。
     *
     * PS：MBeanProxyFactoryBean 创建远程 MBean 的代理。客户端通过此代理与远程 MBean 进行交互，就像它是本地
     * Bean 一样。
     *
     * 例如，考虑如下的 MBeanProxyFactoryBean 声明：
     *
     *     @Bean
     *     public MBeanProxyFactoryBean remoteSpittleControllerMBean(
     *             MBeanServerConnection mbeanServerClient)
     *             throws MalformedObjectNameException {
     *         MBeanProxyFactoryBean proxy = new MBeanProxyFactoryBean();
     *         proxy.setObjectName("");
     *         proxy.setServer(mbeanServerClient);
     *         proxy.setProxyInterface(SpittleControllerManagedOperations.class);
     *         return proxy;
     *     }
     *
     * objectName 属性指定了远程 MBean 的对象名称。在这里是引用之前导出的 SpittleControllerMBean。
     *
     * server 属性引用了 MBeanServerConnection，通过它实现 MBean 所有通信的路由。在这里，注入了之前配置的
     * MBeanServerConnectionFactoryBean。
     *
     * 最后，proxyInterface 属性指定了代理需要实现的接口。在本示例中，使用 SpittleControllerManagedOperations 接口。
     *
     * 对于上面声明的 remoteSpittleControllerMBean，现在可以把它注入到类型为 SpittleControllerManagedOperations
     * 的 bean 属性中，并使用它来访问远程的 MBean。这样，就可以调用 setSpittlesPerPage() 和 getSpittlesPerPage()
     * 方法了。
     *
     * 已经看到与 MBean 通信的几种方式，现在可以在应用运行的时候显示和调整 Spring bean 配置。但是目前为止，这都是单
     * 方面的会话。都是开发者与 MBean 在沟通。现在是时候通过监听通知（notification）来倾听它们在说什么。后续将对此
     * 进行介绍。
     */
    public static void main(String[] args) {

    }

}
