package com.siwuxie095.spring.chapter18th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-03-27 15:15:07
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Spring 的低层级 WebSocket API
     *
     * 按照其最简单的形式，WebSocket 只是两个应用之间通信的通道。位于 WebSocket 一端的应用发送消息，另外一端处理消息。
     * 因为它是全双工的，所以每一端都可以发送和处理消息。
     *
     * PS：WebSocket 是两个应用之间全双工的通信通道。
     *
     * WebSocket 通信可以应用于任何类型的应用中，但是 WebSocket 最常见的应用场景是实现服务器和基于浏览器的应用之间的
     * 通信。浏览器中的 JavaScript 客户端开启一个到服务器的连接，服务器通过这个连接发送更新给浏览器。相比历史上轮询服
     * 务端以查找更新的方案，这种技术更加高效和自然。
     *
     * 为了阐述 Spring 低层级的 WebSocket API，这里编写一个简单的 WebSocket 样例，基于 JavaScript 的客户端与服务
     * 器玩一个无休止的 "Marco Polo" 游戏。服务器端的应用会处理文本消息（"Marco!"），然后在相同的连接上往回发送文本
     * 消息（"Polo!"）。为了在 Spring 使用较低层级的 API 来处理消息，必须编写一个实现 WebSocketHandler 的类：
     *
     * public interface WebSocketHandler {
     *
     *     void afterConnectionEstablished(WebSocketSession session) throws Exception;
     *
     *     void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception;
     *
     *     void handleTransportError(WebSocketSession session, Throwable exception) throws Exception;
     *
     *     void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception;
     *
     *     boolean supportsPartialMessages();
     *
     * }
     *
     * 可以看到，WebSocketHandler 需要实现五个方法。相比直接实现 WebSocketHandler，更为简单的方法是扩展
     * AbstractWebSocketHandler，这是 WebSocketHandler 的一个抽象实现。如下代码展现了 MarcoHandler，
     * 它是 AbstractWebSocketHandler 的一个子类，会在服务器端处理消息。
     *
     * public class MarcoHandler extends AbstractWebSocketHandler {
     *
     *     private static final Logger logger = LoggerFactory.getLogger(MarcoHandler.class);
     *
     *     @Override
     *     protected void handleTextMessage(
     *          WebSocketSession session, TextMessage message) throws Exception {
     *         logger.info("Received message: " + message.getPayload());
     *         Thread.sleep(2000);
     *         session.sendMessage(new TextMessage("Polo!"));
     *     }
     *
     * }
     *
     * 尽管 AbstractWebSocketHandler 是一个抽象类，但是它并不要求必须重载任何特定的方法。相反，它让开发者来决定该重
     * 载哪一个方法。除了重载 WebSocketHandler 中所定义的五个方法以外，还可以重载 AbstractWebSocketHandler 中所
     * 定义的三个方法：
     * （1）handleBinaryMessage()
     * （2）handlePongMessage()
     * （3）handleTextMessage()
     *
     * 这三个方法只是 handleMessage() 方法的具体化，每个方法对应于某一种特定类型的消息。
     *
     * 因为 MarcoHandler 将会处理文本类型的 "Marco!" 消息，因此应该重载 handleTextMessage() 方法。当有文本消息抵
     * 达的时候，日志会记录消息内容，在两秒钟的模拟延迟之后，在同一个连接上返回另外一条文本消息。
     *
     * MarcoHandler 所没有重载的方法都由 AbstractWebSocketHandler 以空操作的方式（no-op）进行了实现。这意味着
     * MarcoHandler 也能处理二进制和 pong 消息，只是对这些消息不进行任何操作而已。
     *
     * 另外一种方案，可以扩展 TextWebSocketHandler，不再扩展 AbstractWebSocketHandler：
     *
     * public class MarcoHandler extends TextWebSocketHandler {
     *
     * // ...
     *
     * }
     *
     * TextWebSocketHandler 是 AbstractWebSocketHandler 的子类，它会拒绝处理二进制消息。它重载了
     * handleBinaryMessage() 方法，如果收到二进制消息的时候，将会关闭 WebSocket 连接。与之类似，
     * BinaryWebSocketHandler 也是 AbstractWebSocketHandler 的子类，它重载了 handleTextMessage()
     * 方法，如果接收到文本消息的话，将会关闭连接。
     *
     * 尽管你会关心如何处理文本消息或二进制消息，或者二者兼而有之，但是你可能还会对建立和关闭连接感兴趣。在本例中，可以重
     * 载 afterConnectionEstablished() 和 afterConnectionClosed()：
     *
     *     @Override
     *     public void afterConnectionEstablished(WebSocketSession session) throws Exception {
     *         logger.info("Connection established");
     *     }
     *
     *     @Override
     *     public void afterConnectionClosed(WebSocketSession session, CloseStatus status)
     *              throws Exception {
     *         logger.info("Connection closed. Status: " + status);
     *     }
     *
     * 这里通过 afterConnectionEstablished() 和 afterConnectionClosed() 方法记录了连接信息。当新连接建立的时候，
     * 会调用 afterConnectionEstablished() 方法，类似地，当连接关闭时，会调用 afterConnectionClosed() 方法。在
     * 本例中，连接事件仅仅记录了日志，但是如果想在连接的生命周期上建立或销毁资源时，这些方法会很有用。
     *
     * 注意，这些方法都是以 "after" 开头。这意味着，这些事件只能在事件发生后才产生响应，因此并不能改变结果。
     *
     * 现在，已经有了消息处理器类，必须要对其进行配置，这样 Spring 才能将消息转发给它。在 Spring 的 Java 配置中，这需
     * 要在一个配置类上使用 @EnableWebSocket，并实现 WebSocketConfigurer 接口，如下所示。
     *
     * @Configuration
     * @EnableWebSocket
     * public class WebSocketConfig implements WebSocketConfigurer {
     *
     *     @Override
     *     public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
     *         registry.addHandler(marcoHandler(), "/marco");
     *     }
     *
     *     @Bean
     *     public MarcoHandler marcoHandler() {
     *         return new MarcoHandler();
     *     }
     *
     * }
     *
     * registerWebSocketHandlers() 方法是注册消息处理器的关键。通过重载该方法，得到了一个 WebSocketHandlerRegistry
     * 对象，通过该对象可以调用 addHandler() 来注册信息处理器。在本例中，注册了 MarcoHandler（以 bean 的方式进行声明）
     * 并将其与 "/marco" 路径相关联。
     *
     * 另外，如果你更喜欢使用 XML 来配置 Spring 的话，那么可以使用 websocket 命名空间：
     *
     *     <websocket:handlers>
     *         <websocket:mapping handler="marcoHandler" path="/marco"/>
     *     </websocket:handlers>
     *
     *     <bean id="marcoHandler"
     *           class="com.siwuxie095.spring.chapter18th.example2nd.marcopolo.MarcoHandler"/>
     *
     * 不管使用 Java 还是使用 XML，这就是所需的配置。
     *
     * 现在，可以把注意力转向客户端，它会发送 "Marco!" 文本消息到服务器，并监听来自服务器的文本消息。如下代码所展示的
     * JavaScript 代码开启了一个原始的 WebSocket 并使用它来发送消息给服务器。
     *
     *       var url = "ws://" + window.location.host + "/websocket/marco";
     *       var sock = new WebSocket(url);
     *
     *       sock.onopen = function() {
     *           console.log('Opening');
     *           sayMarco();
     *       }
     *
     *       sock.onmessage = function(e) {
     *           console.log('Received message: ', e.data);
     *           $('#output').append('Received "' + e.data + '"<br/>');
     *           setTimeout(function(){sayMarco()}, 2000);
     *       }
     *
     *       sock.onclose = function() {
     *           console.log('Closing');
     *       }
     *
     *       function sayMarco() {
     *           console.log('Sending Marco!');
     *           $('#output').append('Sending "Marco!"<br/>');
     *           sock.send("Marco!");
     *       }
     *
     * 在这段代码中，所做的第一件事情就是创建 WebSocket 实例。对于支持 WebSocket 的浏览器来说，这个类型是原生的。通
     * 过创建 WebSocket 实例，实际上打开了到给定 URL 的 WebSocket。在本例中，URL 使用了 "ws://" 前缀，表明这是一
     * 个基本的 WebSocket 连接。如果是安全 WebSocket 的话，协议的前缀将会是 "wss://"。
     *
     * WebSocket 创建完毕之后，接下来的代码建立了 WebSocket 的事件处理功能。注意，WebSocket 的onopen、onmessage
     * 和 onclose 事件对应于 MarcoHandler 的 afterConnectionEstablished()、handleTextMessage() 和
     * afterConnectionClosed() 方法。在 onopen 事件中，设置了一个函数，它会调用 sayMarco() 方法，在该 WebSocket
     * 上发送 "Marco!" 消息。通过发送 "Marco!"，这个无休止的 Marco Polo 游戏就开始了，因为服务器端的 MarcoHandler
     * 作为响应会将 "Polo!" 发送回来，当客户端收到来自服务器的消息后，onmessage 事件会发送另外一个 "Marco!" 给服务器。
     *
     * 这个过程会一直持续下去，直到连接关闭。在这段代码中所没有展示的是如果调用 sock.close() 的话，将会结束这个疯狂的
     * 游戏。在服务端也可以关闭连接，或者浏览器转向其他的页面，都会关闭连接。如果发生以上任意的场景，只要连接关闭，都会
     * 触发 onclose 事件。在这里，出现这种情况将会在控制台日志上记录一条信息。
     *
     * 到此为止，已经编写完使用 Spring 低层级 WebSocket API 的所有代码，包括接收和发送消息的处理器类，以及在浏览器端
     * 完成相同功能的 JavaScript 客户端。如果构建这些代码并将其部署到 Servlet 容器中，那它有可能能够正常运行。
     *
     * 从这里选择 "可能" 这个词，你是不是能够感觉到这里有一点悲观的情绪？这是因为不能保证它可以正常运行。实际上，它很有
     * 可能运行不起来。即便把所有的事情都做对了，诡异的事情依然会困扰开发者。
     *
     * 后续看一下都有什么事情会阻止 WebSocket 正常运行，并采取一些措施提高成功的几率。
     */
    public static void main(String[] args) {

    }

}
