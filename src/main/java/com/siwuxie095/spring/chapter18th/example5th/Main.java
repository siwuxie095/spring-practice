package com.siwuxie095.spring.chapter18th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-03-28 14:15:04
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 启用 STOMP 消息功能
     *
     * 这里将会介绍如何在 Spring MVC 中为控制器方法添加 @MessageMapping 注解，使其处理 STOMP 消息，
     * 它与带有 @RequestMapping 注解的方法处理 HTTP 请求的方式非常类似。但是与 @RequestMapping
     * 不同的是，@MessageMapping 的功能无法通过 @EnableWebMvc 启用。Spring 的 Web 消息功能基于消
     * 息代理（message broker）构建，因此除了告诉 Spring 想要处理消息以外，还有其他的内容需要配置。
     * 必须要配置一个消息代理和其他的一些消息目的地。
     *
     * 如下代码展现了如何通过 Java 配置启用基于代理的 Web 消息功能：
     *
     * @Configuration
     * @EnableWebSocketMessageBroker
     * public class WebSocketStompConfig extends AbstractWebSocketMessageBrokerConfigurer {
     *
     *     @Override
     *     public void registerStompEndpoints(StompEndpointRegistry registry) {
     *         registry.addEndpoint("/marcopolo").withSockJS();
     *     }
     *
     *     @Override
     *     public void configureMessageBroker(MessageBrokerRegistry registry) {
     *         registry.enableSimpleBroker("/queue", "/topic");
     *         registry.setApplicationDestinationPrefixes("/app");
     *     }
     *
     * }
     *
     * 这里 WebSocketStompConfig 使用了 @EnableWebSocketMessageBroker 注解。这表明这个配置类不
     * 仅配置了 WebSocket，还配置了基于代理的 STOMP 消息。它重载了 registerStompEndpoints() 方法，
     * 将 "/marcopolo" 注册为 STOMP 端点。这个路径与之前发送和接收消息的目的地路径有所不同。这是一个
     * 端点，客户端在订阅或发布消息到目的地路径前，要连接该端点。
     *
     * WebSocketStompConfig 还通过重载 configureMessageBroker() 方法配置了一个简单的消息代理。这
     * 个方法是可选的，如果不重载它的话，将会自动配置一个简单的内存消息代理，用它来处理以 "/topic" 为前
     * 缀的消息。但是在本例中，重载了这个方法，所以消息代理将会处理前缀为 "/topic" 和 "/queue" 的消息。
     * 除此之外，发往应用程序的消息将会带有 "/app" 前缀。
     *
     * PS：Spring 简单的 STOMP 代理是基于内存的。
     *
     * 当消息到达时，目的地的前缀将会决定消息该如何处理。这里应用程序的目的地以 "/app" 作为前缀，而代理
     * 的目的地以 "/topic" 和 "/queue" 作为前缀。以应用程序为目的地的消息将会直接路由到带有
     * @MessageMapping 注解的控制器方法中。而发送到代理上的消息，其中也包括 @MessageMapping 注解方
     * 法的返回值所形成的消息，将会路由到代理上，并最终发送到订阅这些目的地的客户端。
     *
     *
     *
     * 启用 STOMP 代理中继
     *
     * 对于初学来讲，简单的代理是很不错的，但是它也有一些限制。尽管它模拟了 STOMP 消息代理，但是它只支持
     * STOMP 命令的子集。因为它是基于内存的，所以它并不适合集群，因为如果集群的话，每个节点也只能管理自
     * 己的代理和自己的那部分消息。
     *
     * 对于生产环境下的应用来说，你可能会希望使用真正支持 STOMP 的代理来支撑 WebSocket 消息，如
     * RabbitMQ 或 ActiveMQ。这样的代理提供了可扩展性和健壮性更好的消息功能，当然它们也会完整支
     * 持 STOMP 命令。这里需要根据相关的文档来为 STOMP 搭建代理。搭建就绪之后，就可以使用 STOMP
     * 代理来替换内存代理了，只需按照如下方式重载 configureMessageBroker() 方法即可：
     *
     *     @Override
     *     public void configureMessageBroker(MessageBrokerRegistry registry) {
     *         registry.enableStompBrokerRelay("/queue", "/topic");
     *         registry.setApplicationDestinationPrefixes("/app");
     *     }
     *
     * 上述 configureMessageBroker() 方法的第一行代码启用了 STOMP 代理中继（broker relay）功能，并
     * 将其目的地前缀设置为 "/topic" 和 "/queue"。这样的话，Spring 就能知道所有目的地前缀为 "/topic"
     * 或 "/queue" 的消息都会发送到 STOMP 代理中。根据你所选择的 STOMP 代理不同，目的地的可选前缀也会
     * 有所限制。例如，RabbitMQ 只允许目的地的类型为 "/temp-queue"、"/exchange"、"/topic"、"/queue"、
     * "/amq/queue" 和 "/reply-queue"。请参阅代理的文档来了解所支持的目的地类型及其使用场景。
     *
     * 除了目的地前缀，在第二行的 configureMessageBroker() 方法中将应用的前缀设置为 "/app"。所有目的
     * 地以 "/app" 打头的消息都将会路由到带有 @MessageMapping 注解的方法中，而不会发布到代理队列或主题
     * 中。
     *
     * 关键的区别在于这里不再模拟 STOMP 代理的功能，而是由代理中继将消息传送到一个真正的消息代理中来进行
     * 处理。
     *
     * 注意，enableStompBrokerRelay() 和 setApplicationDestinationPrefixes() 方法都接收可变长度
     * 的 String 参数，所以可以配置多个目的地和应用前缀。例如：
     *
     *     @Override
     *     public void configureMessageBroker(MessageBrokerRegistry registry) {
     *         registry.enableStompBrokerRelay("/queue", "/topic");
     *         registry.setApplicationDestinationPrefixes("/app", "/foo");
     *     }
     *
     * 默认情况下，STOMP 代理中继会假设代理监听 localhost 的 61613 端口，并且客户端的 username 和
     * password 均为 "guest"。如果你的 STOMP 代理位于其他的服务器上，或者配置成了不同的客户端凭证，
     * 那么可以在启用 STOMP 代理中继的时候，需要配置这些细节信息：
     *
     *     @Override
     *     public void configureMessageBroker(MessageBrokerRegistry registry) {
     *         registry.enableStompBrokerRelay("/queue", "/topic")
     *                 .setRelayHost("rabbit.someotherserver")
     *                 .setRelayPort(62623)
     *                 .setClientLogin("marcopolo")
     *                 .setClientPasscode("letmein01");
     *         registry.setApplicationDestinationPrefixes("/app");
     *     }
     *
     * 以上的这个配置调整了服务器、端口以及凭证信息。但是，并不是必须要配置所有的这些选项。例如，如果你只想
     * 修改中继端口，那么可以只调用 setRelayHost() 方法，在配置中不必使用其他的 Setter 方法。
     *
     * 现在，Spring 已经配置就绪，可以用来处理 STOMP 消息了。
     */
    public static void main(String[] args) {

    }

}
