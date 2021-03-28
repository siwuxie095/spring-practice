package com.siwuxie095.spring.chapter18th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-03-28 15:22:31
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 处理来自客户端的 STOMP 消息
     *
     * Spring MVC 为处理 HTTP Web 请求提供了面向注解的编程模型。@RequestMapping 是 Spring MVC 中最著名的
     * 注解，它会将 HTTP 请求映射到对请求进行处理的方法上。
     *
     * STOMP 和 WebSocket 更多的是关于异步消息，与 HTTP 的请求-响应方式有所不同。但是，Spring 提供了非常类似
     * 于 Spring MVC 的编程模型来处理 STOMP 消息。它非常地相似，以至于对 STOMP 消息的处理器方法也会包含在带有
     * @Controller 注解的类中。
     *
     * Spring 4.0 引入了 @MessageMapping 注解，它用于 STOMP 消息的处理，类似于 Spring MVC 的
     * @RequestMapping 注解。当消息抵达某个特定的目的地时，带有 @MessageMapping 注解的方法能够
     * 处理这些消息。例如，考虑如下代码中的控制器类。
     *
     * @Controller
     * public class MarcoController {
     *
     *     private static final Logger logger = LoggerFactory
     *             .getLogger(MarcoController.class);
     *
     *     @MessageMapping("/marco")
     *     public void handleShout(Shout incoming) {
     *         logger.info("Received message: " + incoming.getMessage());
     *     }
     *
     * }
     *
     * 乍一看上去，它非常类似于其他的 Spring MVC 控制器类。它使用了 @Controller 注解，所以组件扫描能够找到它
     * 并将其注册为 bean。就像其他的 @Controller 类一样，它也包含了处理器方法。
     *
     * 但是这个处理器方法与之前看到的有一点区别。handleShout() 方法没有使用 @RequestMapping 注解，而是使用
     * 了 @MessageMapping 注解。这表示 handleShout() 方法能够处理指定目的地上到达的消息。在本例中，这个目
     * 的地也就是 "/app/marco"（"/app" 前缀是隐含的，因为这里将其配置为应用的目的地前缀）。
     *
     * 因为 handleShout() 方法接收一个 Shout 参数，所以 Spring 的某一个消息转换器会将 STOMP 消息的负载转换
     * 为 Shout 对象。Shout 类非常简单，它是只具有一个属性的 JavaBean，包含了消息的内容：
     *
     * public class Shout {
     *
     *     private String message;
     *
     *     public String getMessage() {
     *         return message;
     *     }
     *
     *     public void setMessage(String message) {
     *         this.message = message;
     *     }
     *
     * }
     *
     * 因为现在处理的不是 HTTP，所以无法使用 Spring 的 HttpMessageConverter 实现将负载转换为 Shout 对象。
     * Spring 4.0 提供了几个消息转换器，作为其消息 API 的一部分。如下描述了这些消息转换器，在处理 STOMP 消息
     * 的时候可能会用到它们。
     * （1）ByteArrayMessageConverter：实现 MIME 类型为 "application/octet-stream" 的消息与 byte[] 之
     * 间的相互转换。
     * （2）MappingJackson2MessageConverter：实现 MIME 类型为 "application/json" 的消息与 Java 对象之
     * 间的相互转换。
     * （3）StringMessageConverter：实现 MIME 类型为 "text/plain" 的消息与 String 之间的相互转换。
     *
     * 假设 handleShout() 方法所处理消息的内容类型为 "application/json"（这应该是一个安全的假设，因为 Shout
     * 不是 byte[] 和 String），MappingJackson2MessageConverter 会负责将 JSON 消息转换为 Shout 对象。就
     * 像在 HTTP 中对应的 MappingJackson2HttpMessageConverter 一样，MappingJackson2MessageConverter
     * 会将其任务委托给底层的 Jackson 2 JSON 处理器。默认情况下，Jackson 会使用反射将 JSON 属性映射为 Java
     * 对象的属性。尽管在本例中没有必要，但是可以通过在 Java 类型上使用 Jackson 注解，影响具体的转换行为。
     *
     *
     *
     * 1、处理订阅
     *
     * 除了 @MessagingMapping 注解以外，Spring 还提供了 @SubscribeMapping 注解。与 @MessagingMapping
     * 注解方法类似，当收到 STOMP 订阅消息的时候，带有 @SubscribeMapping 注解的方法将会触发。
     *
     * 很重要的一点，与 @MessagingMapping 方法类似，@SubscribeMapping 方法也是通过
     * AnnotationMethodMessageHandler 接收消息的。按照 WebSocketStompConfig 的
     * 配置，这就意味着 @SubscribeMapping 方法只能处理目的地以 "/app" 为前缀的消息。
     *
     * 这可能看上去有些诡异，因为应用发出的消息都会经过代理，目的地要以 "/topic" 或 "/queue" 打头。客户端会订
     * 阅这些目的地，而不会订阅前缀为 "/app" 的目的地。如果客户端订阅 "/topic" 和 "/queue" 这样的目的地，那
     * 么 @SubscribeMapping 方法也就无法处理这样的订阅了。如果是这样的话，@SubscribeMapping 有什么用处呢？
     *
     * @SubscribeMapping 的主要应用场景是实现请求-回应模式。在请求-回应模式中，客户端订阅某一个目的地，然后预
     * 期在这个目的地上获得一个一次性的响应。
     *
     * 例如，考虑如下 @SubscribeMapping 注解标注的方法：
     *
     * @SubscribeMapping({"/marco"})
     * public Shout handleSubscription() {
     *     Shout outgoing = new Shout();
     *     outgoing.setMessage("Polo!");
     *     return outgoing;
     * }
     *
     * 可以看到，handleSubscription() 方法使用了 @SubscribeMapping 注解，用这个方法来处理对 "/app/marco"
     * 目的地的订阅（与 @MessageMapping 类似，"/app" 是隐含的）。当处理这个订阅时，handleSubscription()
     * 方法会产生一个输出的 Shout 对象并将其返回。然后，Shout 对象会转换成一条消息，并且会按照客户端订阅时相同
     * 的目的地发送回客户端。
     *
     * 如果你觉得这种请求-回应模式与 HTTP GET 的请求-响应模式并没有太大差别的话，那么你基本上是正确的。但是，这
     * 里的关键区别在于 HTTP GET 请求是同步的，而订阅的请求-回应模式则是异步的，这样客户端能够在回应可用时再去处
     * 理，而不必等待。
     *
     *
     *
     * 2、编写 JavaScript 客户端
     *
     * handleShout() 方法已经可以处理发送过来的消息了。现在，需要的就是发送消息的客户端。
     *
     * 如下的代码展现了一些 JavaScript 客户端代码，它会连接 "/marcopolo" 端点并发送 "Marco!" 消息。
     *
     * var url = 'http://' + window.location.host + '/stomp/marcopolo';
     * var sock = new SockJS(url);
     *
     * var stomp = new Stomp.over(sock);
     * var payload = JSON.stringify({'message':'Marco!'});
     *
     * stomp.connect('guest', 'guest', function(frame) {
     *     stomp.send("/marco", {}, payload);
     * });
     *
     * 与之前的 JavaScript 客户端样例类似，在这里首先针对给定的 URL 创建一个 SockJS 实例。在本例中，URL 引用
     * 的是配置的 STOMP 端点（不包括应用的上下文路径 "/stomp"）。
     *
     * 但是，这里的区别在于，不再直接使用 SockJS，而是通过调用 Stomp.over(sock) 创建了一个 STOMP 客户端实例。
     * 这实际上封装了 SockJS，这样就能在 WebSocket 连接上发送 STOMP 消息。
     *
     * 接下来，使用 STOMP 进行连接，假设连接成功，然后发送带有 JSON 负载的消息到名为 "/marco" 的目的地。往
     * send() 方法传递的第二个参数是一个头信息的 Map，它会包含在 STOMP 的帧中，不过在这个例子中，没有提供任
     * 何参数，Map 是空的。
     *
     * 现在，有了能够发送消息到服务器的客户端，以及用来处理消息的服务端处理器方法。这是一个好的开端，但是你可能已
     * 经发现这都是单向的。后续会让服务器发出的声音，看一下如何发送消息给客户端。
     */
    public static void main(String[] args) {

    }

}
