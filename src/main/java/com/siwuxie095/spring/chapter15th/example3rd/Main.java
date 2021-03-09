package com.siwuxie095.spring.chapter15th.example3rd;

/**
 * @author Jiajing Li
 * @date 2021-03-09 08:16:37
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 RMI
     *
     * 如果你已经使用 Java 编程有些年头的话，你肯定会听说过（也可能使用过）RMI。RMI 最初在 JDK 1.1 被引入到
     * Java 平台中，它为 Java 开发者提供了一种强大的方法来实现 Java 程序间的交互。在 RMI 之前，对于 Java
     * 开发者来说，远程调用的唯一选择就是 CORBA（在当时，需要购买一种第三方产品，叫作 Object Request Broker
     * [ORB]），或者手工编写 Socket 程序。
     *
     * 但是开发和访问 RMI 服务是非常乏味无聊的，它涉及到好几个步骤，包括程序的和手工的。Spring 简化了 RMI 模
     * 型，它提供了一个代理工厂 bean，能把 RMI 服务像本地 JavaBean 那样装配到 Spring 应用中。Spring 还提
     * 供了一个远程导出器，用来简化把 Spring 管理的 bean 转换为 RMI 服务的工作。
     *
     * 对于 Spittr 应用，这里将展示如何把一个 RMI 服务装配进客户端应用程序的 Spring 应用上下文中。但首先，来
     * 看看如何使用 RMI 导出器把 SpitterService 的实现发布为 RMI 服务。
     *
     *
     *
     * 1、导出 RMI 服务
     *
     * 如果你曾经创建过 RMI 服务，应该会知道这会涉及如下几个步骤：
     * （1）编写一个服务实现类，类中的方法必须抛出 java.rmi.RemoteException 异常；
     * （2）创建一个继承于 java.rmi.Remote 的服务接口；
     * （3）运行 RMI 编译器（rmic），创建客户端 stub 类和服务端 skeleton 类；
     * （4）启动一个 RMI 注册表，以便持有这些服务；
     * （5）在 RMI 注册表中注册服务。
     *
     * 可以看到，发布一个简单的 RMI 服务需要做这么多的工作。除了这些必需的步骤外，你可能注意到了，会抛出相当多
     * 的 RemoteException 和 MalformedURLException 异常。虽然这些异常通常意味着一个无法从 catch 代码块
     * 中恢复的致命错误，但是仍然需要编写样板式的代码来捕获并处理这些异常 —— 即使不能修复它们。
     *
     * 很明显，发布一个 RMI 服务涉及到大量的代码和手工作业。Spring 是否能够做一些工作来让这些事情变得不再那么
     * 棘手呢？
     *
     *
     * 1.1、在 Spring 中配置 RMI 服务
     *
     * 幸运的是，Spring 提供了更简单的方式来发布 RMI 服务，不用再编写那些需要抛出 RemoteException 异常的特
     * 定 RMI 类，只需简单地编写实现服务功能的 POJO 就可以了，Spring 会处理剩余的其他事项。
     *
     * 这里将要创建的 RMI 服务需要发布 SpitterService 接口中的方法，该接口定义见 SpitterService。
     *
     * 如果使用传统的 RMI 来发布此服务，SpitterService 和 SpitterServiceImpl 中的所有方法都需要抛出 java.
     * rmi.RemoteException。但是如果使用 Spring 的 RmiServiceExporter 把该类转变为 RMI 服务，那现有的
     * 实现不需要做任何改变。
     *
     * RmiServiceExporter 可以把任意 Spring 管理的 bean 发布为 RMI 服务。RmiServiceExporter 把 bean
     * 包装在一个适配器类中，然后适配器类被绑定到 RMI 注册表中，并且代理到服务类的请求 —— 在本例中服务类也就是
     * SpitterServiceImpl。
     *
     * PS：RmiServiceExporter 把 POJO 包装到服务适配器中，并将服务适配器绑定到 RMI 注册表中，从而将 POJO
     * 转换为 RMI 服务。
     *
     * 使用 RmiServiceExporter 将 SpitterServiceImpl 发布为 RMI 服务的最简单方式是在 Spring 中使用如下
     * 的 @Bean 方法进行配置：
     *
     *     @Bean
     *     public RmiServiceExporter rmiExporter(SpitterService spitterService) {
     *         RmiServiceExporter rmiExporter = new RmiServiceExporter();
     *         rmiExporter.setService(spitterService);
     *         rmiExporter.setServiceName("SpitterService");
     *         rmiExporter.setServiceInterface(SpitterService.class);
     *         return rmiExporter;
     *     }
     *
     * 这里会把 spitterService bean 设置到 service 属性中，表明 RmiServiceExporter 要把该 bean 发布为
     * 一个 RMI 服务。serviceName 属性命名了 RMI 服务，serviceInterface 属性指定了此服务所实现的接口。
     *
     * 默认情况下，RmiServiceExporter 会尝试绑定到本地机器 1099 端口上的 RMI 注册表。如果在这个端口没有发
     * 现 RMI 注册表，RmiServiceExporter 将会启动一个注册表。如果希望绑定到不同端口或主机上的 RMI 注册表，
     * 那么可以通过 registryPort 和 registryHost 属性来指定。例如，下面的 RmiServiceExporter 会尝试绑定
     * rmi.spitter.com 主机 1199 端口上的 RMI 注册表：
     *
     *     @Bean
     *     public RmiServiceExporter rmiExporter(SpitterService spitterService) {
     *         RmiServiceExporter rmiExporter = new RmiServiceExporter();
     *         rmiExporter.setService(spitterService);
     *         rmiExporter.setServiceName("SpitterService");
     *         rmiExporter.setServiceInterface(SpitterService.class);
     *         rmiExporter.setRegistryHost("rmi.spitter.com");
     *         rmiExporter.setRegistryPort(1199);
     *         return rmiExporter;
     *     }
     *
     * 这就是使用 Spring 把某个 bean 转变为 RMI 服务所需要做的全部工作。现在 Spitter 服务已经导出为 RMI 服
     * 务，可以为 Spittr 应用创建其他的用户界面或邀请第三方使用此 RMI 服务创建新的客户端。如果使用 Spring，客
     * 户端开发者访问 Spitter 的 RMI 服务会非常容易。
     *
     * 下面转换一下视角，来看看如何编写 Spitter RMI 服务的客户端。
     *
     *
     *
     * 2、装配 RMI 服务
     *
     * 传统上，RMI 客户端必须使用 RMI API 的 Naming 类从 RMI 注册表中查找服务。例如，下面的代码片段演示了如
     * 何获取 Spitter 的 RMI 服务：
     *
     * try {
     *     String serviceUrl = "rmi:/spitter/SpitterService";
     *     SpitterService spitterService = Naming.lookup(serviceUrl);
     *     ...
     * } catch (RemoteException e) {
     *     ...
     * } catch (NotBoundException e) {
     *     ...
     * } catch (MalformedURLException e) {
     *     ...
     * }
     *
     * 虽然这段代码可以获取 Spitter 的 RMI 服务的引用，但是它存在两个问题：
     * （1）传统的 RMI 查找可能会导致以上 3 种检查型异常的任意一种，这些异常必须被捕获或重新抛出；
     * （2）需要 Spitter 服务的任何代码都必须自己负责获取该服务。这属于样板代码，与客户端的功能并没有直接关系。
     *
     * RMI 查找过程中所抛出的异常通常意味着应用发生了致命的不可恢复的问题。例如，MalformedURLException 异常
     * 意味着这个服务的地址是无效的。为了从这个异常中恢复，应用至少要重新配置，也可能需要重新编译。try/catch
     * 代码块并不能在发生异常时优雅地恢复，既然如此，为什么还要强制代码捕获并处理这个异常呢？
     *
     * 但是，更糟糕的事情是这段代码直接违反了依赖注入（DI）原则。因为客户端代码需要负责查找 Spitter 服务，并且
     * 这个服务是 RMI 服务，这里甚至没有任何机会去提供 SpitterService 对象的不同实现。理想情况下，应该可以为
     * 任意一个 bean 注入 SpitterService 对象，而不是让 bean 自己去查找服务。利用 DI，SpitterService 的
     * 任何客户端都不需要关心此服务来源于何处。
     *
     * Spring 的 RmiProxyFactoryBean 是一个工厂 bean，该 bean 可以为 RMI 服务创建代理。
     *
     * 使用 RmiProxyFactoryBean 引用 SpitterService 的 RMI 服务是非常简单的，只需要在客户端的 Spring 配
     * 置中增加如下的 @Bean 方法：
     *
     *     @Bean
     *     public RmiProxyFactoryBean spitterService() {
     *         RmiProxyFactoryBean proxyFactoryBean = new RmiProxyFactoryBean();
     *         proxyFactoryBean.setServiceUrl("rmi://localhost/SpitterService");
     *         proxyFactoryBean.setServiceInterface(SpitterService.class);
     *         return proxyFactoryBean;
     *     }
     *
     * 服务的 URL 是通过 RmiProxyFactoryBean 的 serviceUrl 属性来设置的。在这里，服务名被设置为
     * SpitterService，并且声明服务是在本地机器上的；同时，服务提供的接口由 serviceInterface 属性
     * 来指定。
     *
     * PS：RmiProxyFactoryBean 生成一个代理对象，该对象代表客户端来负责与远程的 RMI 服务进行通信。
     * 客户端通过服务的接口与代理进行交互，就如同远程服务就是一个本地的 POJO。
     *
     * 现在已经把 RMI 服务声明为 Spring 管理的 bean，就可以把它作为依赖装配进另一个 bean 中，就像任意非远程
     * 的 bean 那样。例如，假设客户端需要使用 Spitter 服务为指定的用户获取 Spittle 列表，可以使用 @Autowired
     * 注解把服务代理装配进客户端中：
     *
     *     @Autowired
     *     private SpitterService spitterService;
     *
     * 还可以像本地 bean 一样调用它的方法：
     *
     * public List<Spittle> getSpittles(String username) {
     *     Spitter spitter = spitterService.getSpitter(username);
     *     return spitterService.getSpittlesForSpitter(spitter);
     * }
     *
     * 以这种方式访问 RMI 服务简直太棒了！客户端代码甚至不需要知道所处理的是一个 RMI 服务。它只是通过注入机制
     * 接受了一个 SpitterService 对象，根本不必关心它来自何处。实际上，谁知道客户端得到的就是一个基于 RMI
     * 的实现呢？
     *
     * 此外，代理捕获了这个服务所有可能抛出的 RemoteException 异常，并把它包装为运行期异常重新抛出，这样就可
     * 以放心地忽略这些异常。也可以非常容易地把远程服务 bean 替换为该服务的其他实现 —— 或许是不同的远程服务，
     * 或者可能是客户端代码单元测试时的一个 mock 实现。
     *
     * 虽然客户端代码根本不需要关心所赋予的 SpitterService 是一个远程服务，但需要非常谨慎地设计远程服务的接口。
     * 提醒一下，客户端不得不调用两次服务：一次是根据用户名查找 Spitter，另一次是获取 Spittle 对象的列表。这
     * 两次远程调用都会受网络延迟的影响，进而可能会影响到客户端的性能。清楚了客户端是如何使用服务的，或许会重写
     * 接口，把这两个调用放进一个方法中。但是现在暂时要接受这样的服务接口。
     *
     * RMI 是一种实现远程服务交互的好办法，但是它存在某些限制。首先，RMI 很难穿越防火墙，这是因为 RMI 使用任意
     * 端口来交互 —— 这是防火墙通常所不允许的。在企业内部网络环境中，通常不需要担心这个问题。但是如果在互联网上
     * 运行，用 RMI 可能会遇到麻烦。即使 RMI 提供了对 HTTP 的通道的支持（通常防火墙都允许），但是建立这个通道
     * 也不是件容易的事。
     *
     * 另外一件需要考虑的事情是 RMI 是基于 Java 的。这意味着客户端和服务端必须都是用 Java 开发的。因为 RMI 使
     * 用了 Java 的序列化机制，所以通过网络传输的对象类型必须要保证在调用两端的 Java 运行时中是完全相同的版本。
     * 对应用而言，这可能是个问题，也可能不是问题。但是选择 RMI 做远程服务时，必须要牢记这一点。
     *
     * Caucho Technology（Resin 应用服务器背后的公司）开发了一套应对 RMI 限制的远程调用解决方案。
     *
     * 实际上，Caucho 提供了两种解决方案：Hessian 和 Burlap。后续将会介绍如何在 Spring 中使用 Hessian 和
     * Burlap 处理远程服务。
     */
    public static void main(String[] args) {

    }

}
