package com.siwuxie095.spring.chapter18th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-03-28 16:53:49
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 为目标用户发送消息
     *
     * 到目前为止，所发送和接收的消息都是客户端（在 Web 浏览器中）和服务器端之间的，并没有考虑到客户端的用户。当带有
     * @MessageMapping 注解的方法触发时，知道收到了消息，但是并不知道消息来源于谁。类似地，因为不知道用户是谁，所
     * 以消息会发送到所有订阅对应主题的客户端上，没有办法发送消息给指定用户。
     *
     * 但是，如果你知道用户是谁的话，那么就能处理与某个用户相关的消息，而不仅仅是与所有客户端相关联。好消息是已经了解
     * 了如何识别用户。通过使用认证机制，可以使用 Spring Security 来认证用户，并为目标用户处理消息。
     *
     * 在使用 Spring 和 STOMP 消息功能的时候，有三种方式利用认证用户：
     * （1）@MessageMapping 和 @SubscribeMapping 标注的方法能够使用 Principal 来获取认证用户；
     * （2）@MessageMapping、@SubscribeMapping 和 @MessageException 方法返回的值能够以消息的形式发送给认证用
     * 户；
     * （3）SimpMessagingTemplate 能够发送消息给特定用户。
     *
     * 首先看一下前两种方式，它们都能让控制器的消息处理方法使用针对特定用户的消息。
     *
     *
     *
     * 1、在控制器中处理用户的消息
     *
     * 如前所述，在控制器的 @MessageMapping 或 @SubscribeMapping 方法中，处理消息时有两种方式了解用户信息。在处
     * 理器方法中，通过简单地添加一个 Principal 参数，这个方法就能知道用户是谁并利用该信息关注此用户相关的数据。除此
     * 之外，处理器方法还可以使用 @SendToUser 注解，表明它的返回值要以消息的形式发送给某个认证用户的客户端（只发送给
     * 该客户端）。
     *
     * 为了阐述该功能，编写一个控制器方法，它会根据传入的消息创建新的 Spittle 对象，并发送一个回应，表明 Spittle 已
     * 经保存成功。如果你觉得这个场景很熟悉的话，那是因为之前以 REST 端点的形式实现了它。但是 REST 请求是同步的，当
     * 服务器处理的时候，客户端必须要等待。通过将 Spittle 发送为 STOMP 消息，可以充分发挥 STOMP 消息异步的优势。
     *
     * 考虑如下的 handleSpittle() 方法，它会处理传入的消息并将其存储为 Spittle：
     *
     *     @MessageMapping("/spittle")
     *     @SendToUser("/queue/notifications")
     *     public Notification handleSpittle(Principal principal, SpittleForm form) {
     *         Spittle spittle = new Spittle(principal.getName(), form.getText(), new Date());
     *         spittleRepo.save(spittle);
     *         return new Notification("Saved Spittle");
     *     }
     *
     * 可以看到，handleSpittle() 方法接受 Principal 对象和 SpittleForm 对象作为参数。它使用这两个对象创建一个
     * Spittle 实例并借助 SpittleRepository 将实例保存起来。最后，它返回一个新的 Notification，表明 Spittle
     * 已经保存成功。
     *
     * 当然，比起方法内部的功能，这个方法体外部所做事情也许更令人感兴趣。因为这个方法使用了 @MessageMapping 注解，
     * 因此当有发往 "/app/spittle" 目的地的消息到达时，该方法就会触发，并且会根据消息创建 SpittleForm 对象，如
     * 果用户已经认证过的话，将会根据 STOMP 帧上的头信息得到 Principal 对象。
     *
     * 但是，需要特别关注的是，返回的 Notification 到哪里去了。@SendToUser 注解指定返回的 Notification 要以
     * 消息的形式发送到 "/queue/notifications" 目的地上。在表面上，"/queue/notifications" 并没有与特定用户
     * 关联。但因为这里使用的是 @SendToUser 注解而不是 @SendTo，所以就会发生更多的事情了。
     *
     * 为了理解 Spring 如何发布消息，不妨先退后一步，看一下针对控制器方法发布 Notification 对象的目的地，客户端
     * 该如何进行订阅。考虑如下的这行 JavaScript 代码，它订阅了一个用户特定的目的地：
     *
     * stomp.subscribe("/user/queue/notifications", handleNotifications);
     *
     * 注意，这个目的地使用了 "/user" 作为前缀，在内部，以 "/user" 作为前缀的目的地将会以特殊的方式进行处理。这
     * 种消息不会通过 AnnotationMethodMessageHandler（像应用消息那样）来处理，也不会通过
     * SimpleBrokerMessageHandler 或 StompBrokerRelayMessageHandler（像代理消息那样）来处理，以 "/user"
     * 为前缀的消息将会通过 UserDestinationMessageHandler 进行处理。
     *
     * UserDestinationMessageHandler 的主要任务是将用户消息重新路由到某个用户独有的目的地上。在处理订阅的时候，
     * 它会将目标地址中的 "/user" 前缀去掉，并基于用户的会话添加一个后缀。例如，对 "/user/queue/notifications"
     * 的订阅最后可能路由到名为 "/queue/notifications-user6hr83v6t" 的目的地上。
     *
     * 在这里的样例中，handleSpittle() 方法使用了 @SendToUser("/queue/notifications") 注解。这个新的目的地
     * 以 "/queue" 作为前缀，根据配置，这是 StompBrokerRelayMessageHandler（或SimpleBrokerMessageHandler）
     * 要处理的前缀，所以消息接下来会到达这里。最终，客户端会订阅这个目的地，因此客户端会收到 Notification 消息。
     *
     * 在控制器方法中，@SendToUser注解和Principal参数是很有用的。但是在上面的代码中，看到借助消息模板，可以在应
     * 用的任何位置发送消息。接下来看一下如何使用 SimpMessagingTemplate 将消息发送给特定用户。
     *
     *
     *
     * 2、为指定用户发送消息
     *
     * 除了 convertAndSend() 以外，SimpMessagingTemplate 还提供了 convertAndSendToUser() 方法。按照名字
     * 就可以判断出来，convertAndSendToUser() 方法能够给特定用户发送消息。
     *
     * 为了阐述该功能，要在 Spittr 应用中添加一项特性，当其他用户提交的 Spittle 提到某个用户时，将会提醒该用户。
     * 例如，如果 Spittle 文本中包含 "@jbauer"，那么就应该发送一条消息给使用 "jbauer" 用户名登录的客户端。如下
     * 代码中的 broadcastSpittle() 方法使用了 convertAndSendToUser()，从而能够提醒所谈论到的用户。
     *
     * @Service
     * public class SpittleFeedServiceImpl implements SpittleFeedService {
     *
     *     private SimpMessagingTemplate messaging;
     *     private Pattern pattern = Pattern.compile("\\@(\\S+)");
     *
     *     @Autowired
     *     public SpittleFeedServiceImpl(SimpMessagingTemplate messaging) {
     *         this.messaging = messaging;
     *     }
     *
     *     @Override
     *     public void broadcastSpittle(Spittle spittle) {
     *         messaging.convertAndSend("/topic/spittlefeed", spittle);
     *
     *         Matcher matcher = pattern.matcher(spittle.getMessage());
     *         if (matcher.find()) {
     *             String username = matcher.group(1);
     *             messaging.convertAndSendToUser(username, "/queue/notifications",
     *                     new Notification("You just got mentioned!"));
     *         }
     *     }
     *
     * }
     *
     * 在 broadcastSpittle() 中，如果给定 Spittle 对象的消息中包含了类似于用户名的内容（也就是以 "@" 开头的文
     * 本），那么一个新的 Notification 将会发送到名为 "/queue/notifications" 的目的地上。因此，如果 Spittle
     * 中包含 "@jbauer" 的话，Notification 将会发送到 "/user/jbauer/queue/notifications" 目的地上。
     */
    public static void main(String[] args) {

    }

}
