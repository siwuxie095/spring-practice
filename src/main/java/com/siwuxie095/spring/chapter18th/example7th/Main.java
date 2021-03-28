package com.siwuxie095.spring.chapter18th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-03-28 16:03:47
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 发送消息到客户端
     *
     * 到目前为止，客户端负责了所有的消息发送，服务器只能监听这些消息。对于 WebSocket 和 STOMP 来说，这是一种合法的
     * 用法，但是当你考虑使用 WebSocket 的时候，所设想的使用场景恐怕并非如此。WebSocket 通常视为服务器发送数据给浏
     * 览器的一种方式，采用这种方式所发送的数据不必位于 HTTP 请求的响应中。使用 Spring 和 WebSocket/STOMP 的话，
     * 该如何与基于浏览器的客户端通信呢？
     *
     * Spring 提供了两种发送数据给客户端的方法：
     * （1）作为处理消息或处理订阅的附带结果；
     * （2）使用消息模板。
     *
     * 已经了解了一些处理消息和处理订阅的方法，所以首先看一下如何通过这些方法发送消息给客户端。然后，再看一下 Spring
     * 的 SimpMessagingTemplate，它能够在应用的任何地方发送消息。
     *
     *
     *
     * 1、在处理消息之后，发送消息
     *
     * 之前的 handleShout() 只是简单地返回 void。它的任务就是处理消息，并不需要给客户端回应。
     *
     * 如果你想要在接收消息的时候，同时在响应中发送一条消息，那么需要做的仅仅是将内容返回就可以了，方法签名不再是使用
     * void。例如，如果你想发送 "Polo!" 消息作为 "Marco!" 消息的回应，那么只需将 handleShout() 修改为如下所示：
     *
     *     @MessageMapping("/marco")
     *     public Shout handleShout(Shout incoming) {
     *         logger.info("Received message: " + incoming.getMessage());
     *
     *         Shout outgoing = new Shout();
     *         outgoing.setMessage("Polo!");
     *         return outgoing;
     *     }
     *
     * 在这个新版本的 handleShout() 方法中，会返回一个新的 Shout 对象。通过简单地返回一个对象，处理器方法同时也变
     * 成了发送方法。当 @MessageMapping 注解标示的方法有返回值的时候，返回的对象将会进行转换（通过消息转换器）并放
     * 到 STOMP 帧的负载中，然后发送给消息代理。
     *
     * 默认情况下，帧所发往的目的地会与触发处理器方法的目的地相同，只不过会添加上 "/topic" 前缀。就本例而言，这意味
     * 着 handleShout() 方法所返回的 Shout 对象会写入到 STOMP 帧的负载中，并发布到 "/topic/marco" 目的地。不过，
     * 可以通过为方法添加 @SendTo 注解，重载目的地：
     *
     *     @MessageMapping("/marco")
     *     @SendTo("/topic/shout")
     *     public Shout handleShout(Shout incoming) {
     *         logger.info("Received message: " + incoming.getMessage());
     *
     *         Shout outgoing = new Shout();
     *         outgoing.setMessage("Polo!");
     *         return outgoing;
     *     }
     *
     * 按照这个 @SendTo 注解，消息将会发布到 "/topic/shout"。所有订阅这个主题的应用（如客户端）都会收到这条消息。
     *
     * 这样的话，handleShout() 在收到一条消息的时候，作为响应也会发送一条消息。按照类似的方式，@SubscribeMapping
     * 注解标注的方式也能发送一条消息，作为订阅的回应。例如，通过为控制器添加如下的方法，当客户端订阅的时候，将会发送
     * 一条 Shout 信息：
     *
     * @SubscribeMapping("/marco")
     * public Shout handleSubscription() {
     *     Shout outgoing = new Shout();
     *     outgoing.setMessage("Polo!");
     *     return outgoing;
     * }
     *
     * 这里的 @SubscribeMapping 注解表明当客户端订阅 "/app/marco"（"/app" 是应用目的地的前缀）目的地的时候，将
     * 会调用 handleSubscription() 方法。它所返回的 Shout 对象将会进行转换并发送回客户端。
     *
     * @SubscribeMapping 的区别在于这里的 Shout 消息将会直接发送给客户端，而不必经过消息代理。如果你为方法添加
     * @SendTo 注解的话，那么消息将会发送到指定的目的地，这样会经过代理。
     *
     *
     *
     * 2、在应用的任意地方发送消息
     *
     * @MessageMapping 和 @SubscribeMapping 提供了一种很简单的方式来发送消息，这是接收消息或处理订阅的附带结果。
     * 不过，Spring 的 SimpMessagingTemplate 能够在应用的任何地方发送消息，甚至不必以首先接收一条消息作为前提。
     *
     * 使用 SimpMessagingTemplate 的最简单方式是将它（或者其接口 SimpMessageSendingOperations）自动装配到所
     * 需的对象中。
     *
     * 为了将这一切付诸实施，重新看一下 Spittr 的首页，为其提供实时的 Spittle feed 功能。按照其当前的写法，控制器
     * 会处理首页的请求，将最新的 Spittle 列表获取到，并将其放到模型中，然后渲染到用户的浏览器中。
     *
     * 尽管这样运行起来也不错，但是它并没有提供 Spittle 更新的实时 feed。如果用户想要看一个更新的 Spittle feed，
     * 那必须要在浏览器中刷新页面。
     *
     * 这里不必要求用户刷新页面，而是让首页订阅一个 STOMP 主题，在 Spittle 创建的时候，该主题能够收到 Spittle 更
     * 新的实时 feed。在首页中，需要添加如下的 JavaScript 代码块：
     *
     * <script>
     *     var sock = new SockJS('spittr');
     *     var stomp = Stomp.over(sock);
     *
     *     stomp.connect('guest', 'guest', function(frame) {
     *         console.log('Connected');
     *         stomp.subscribe("/topic/spittlefeed", handleSpittle);
     *     });
     *
     *     function handleSpittle(incoming) {
     *         var spittle = JSON.parse(incoming.body);
     *         console.log('Received: ', spittle);
     *         var source = $("#spittle-template").html();
     *         var template = Handlebars.compile(source);
     *         var spittleHtml = template(spittle);
     *         $('.spittleList').prepend(spittleHtml);
     *     }
     * </script>
     *
     * 与之前的样例一样，首先创建了 SockJS 实例，然后基于该 SockJS 实例创建了 Stomp 实例。在连接到 STOMP 代理之
     * 后，订阅了 "/topic/spittlefeed"，并指定当消息达到的时候，由 handleSpittle() 函数来处理 Spittle 更新。
     * handleSpittle() 函数会将传入的消息体解析为对应的 JavaScript 对象，然后使用 Handlebars 库将 Spittle 数
     * 据渲染为 HTML 并插入到列表中。Handlebars 模板定义在一个单独的 <script> 标签中，如下所示：
     *
     * <script id="spittle-template" type="text/x-handlebars-template">
     *     <li id="preexist">
     *         <div class="spittleMessage">{{message}}</div>
     *         <div>
     *             <span class="spittleTime">{{message}}</span>
     *             <span class="spittleLocation">{{latitude}}, {{longitude}}</span>
     *         </div>
     *     </li>
     * </script>
     *
     * 在服务器端，可以使用 SimpMessagingTemplate 将所有新创建的 Spittle 以消息的形式发布到 "/topic/spittlefeed"
     * 主题上。如下代码展现的 SpittleFeedServiceImpl 就是实现该功能的简单服务：
     *
     * @Service
     * public class SpittleFeedServiceImpl implements SpittleFeedService {
     *
     *     private SimpMessageSendingOperations messaging;
     *
     *     @Autowired
     *     public SpittleFeedServiceImpl(SimpMessageSendingOperations messaging) {
     *         this.messaging = messaging;
     *     }
     *
     *     @Override
     *     public void broadcastSpittle(Spittle spittle) {
     *         messaging.convertAndSend("/topic/spittefeed", spittle);
     *     }
     * }
     *
     * 配置 Spring 支持 STOMP 的一个副作用就是在 Spring 应用上下文中已经包含了 SimpMessagingTemplate。因此，
     * 在这里没有必要再创建新的实例。SpittleFeedServiceImpl 的构造器使用了 @Autowired 注解，这样当创建
     * SpittleFeedService-Impl 的时候，就能注入 SimpMessagingTemplate（以 SimpMessageSendingOperations
     * 的形式）了。
     *
     * 发送 Spittle 消息的地方在 broadcastSpittle() 方法中。它在注入的 SimpMessageSendingOperations 上调用
     * 了 convertAndSend() 方法，将 Spittle 转换为消息，并将其发送到 "/topic/spittlefeed" 主题上。如果你觉得
     * convertAndSend() 方法看起来很眼熟的话，那是因为它模拟了 JmsTemplate 和 RabbitTemplate 所提供的同名方法。
     *
     * 不管通过 convertAndSend() 方法，还是借助处理器方法的结果，在发布消息给 STOMP 主题的时候，所有订阅该主题的
     * 客户端都会收到消息。在这个场景下，希望所有的客户端都能及时看到实时的 Spittle feed，这种做法是很好的。但有的
     * 时候，希望发送消息给指定的用户，而不是所有的客户端。
     */
    public static void main(String[] args) {

    }

}
