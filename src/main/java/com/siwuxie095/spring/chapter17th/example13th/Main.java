package com.siwuxie095.spring.chapter17th.example13th;

/**
 * @author Jiajing Li
 * @date 2021-03-26 08:07:18
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 RabbitTemplate 发送消息
     *
     * 顾名思义，RabbitMQ 连接工厂的作用是创建到 RabbitMQ 的连接。如果你希望通过 RabbitMQ 发送消息，那么你可以将
     * connectionFactory bean 注入到 AlertServiceImpl 类中，并使用它来创建 Connection，使用这个 Connection
     * 来创建 Channel，然后使用这个 Channel 发布消息到 Exchange 上。
     *
     * 是的，你的确可以这样做。
     *
     * 但是，如果这样做的话，你要做许多的工作并且会涉及到很多样板式代码。Spring 所讨厌的一件事情就是样板式代码。已经
     * 看到 Spring 提供模板来消除样板式代码的多个例子 —— 包括之前所介绍的 JmsTemplate，它消除了 JMS 的样板式代码。
     * 因此，Spring AMQP 提供 RabbitTemplate 来消除 RabbitMQ 发送和接收消息相关的样板式代码就一点也不让人感觉奇
     * 怪了。
     *
     * 配置 RabbitTemplate 的最简单方式是使用 rabbit 命名空间的 <template> 元素，如下所示：
     *
     * <template id="rabbitTemplate" connection-factory="connectionFactory" />
     *
     * 现在，要发送消息的话，只需要将模板 bean 注入到 AlertServiceImpl 中，并使用它来发送 Spittle。如下代码展现
     * 了一个新版本的 AlertServiceImpl，它使用 RabbitTemplate 代替 JmsTemplate 来发送 Spittle 提醒。
     *
     * public class AlertServiceImpl implements AlertService {
     *
     *     private RabbitTemplate rabbit;
     *
     *     @Autowired
     *     public AlertServiceImpl(RabbitTemplate rabbit) {
     *         this.rabbit = rabbit;
     *     }
     *
     *     @Override
     *     public void sendSpittleAlert(Spittle spittle) {
     *         rabbit.convertAndSend("spittle.alert.exchange",
     *                 "spittle.alerts",
     *                 spittle);
     *     }
     *
     * }
     *
     * 可以看到，现在 sendSpittleAlert() 调用 RabbitTemplate 的 convertAndSend() 方法，其中 RabbitTemplate
     * 是被注入进来的。它传入了三个参数：Exchange 的名称、routing key 以及要发送的对象。注意，这里并没有指定消息该
     * 路由到何处、要发送给哪个队列以及期望哪个消费者来获取消息。
     *
     * RabbitTemplate 有多个重载版本的 convertAndSend() 方法，这些方法可以简化它的使用。例如，使用某个重载版本的
     * convertAndSend() 方法，可以在调用 convertAndSend() 的时候，不设置 Exchange 的名称：
     *
     * rabbit.convertAndSend("spittle.alerts", spittle);
     *
     *
     * 如果你愿意的话，还可以同时省略 Exchange 名称和 routing key：
     *
     * rabbit.convertAndSend(spittle);
     *
     *
     * 如果在参数列表中省略 Exchange 名称，或者同时省略 Exchange 名称和 routing key 的话，RabbitTemplate 将会
     * 使用默认的 Exchange 名称和 routing key。按照之前的配置，默认的 Exchange 名称为空（或者说是默认没有名称的
     * 那一个Exchange），默认的 routing key 也为空。但是，可以在 <template> 元素上借助 exchange 和 routing-key
     * 属性配置不同的默认值：
     *
     * <template id="rabbitTemplate"
     *          connection-factory="connectionFactory"
     *          exchange="spittle.alert.exchange"
     *          routing-key="spittle.alerts" />
     *
     * 不管设置的默认值是什么，都可以在调用 convertAndSend() 方法的时候，以参数的形式显式指定它们，从而覆盖掉默认值。
     *
     * RabbitTemplate 还有其他的方法来发送消息，你可能会对此感兴趣。例如，可以使用较低等级的 send() 方法来发送 org.
     * springframework.amqp.core.Message 对象，如下所示：
     *
     * Message hellMessage = new Message("Hello World!".getBytes(), new MessageProperties());
     * rabbit.send("hello.exchange", "hello.routing", helloMessage);
     *
     * 与 convertAndSend() 方法类似，send() 方法也有重载形式，它们不需要提供 Exchange 名称和/或 routing key。
     *
     * 使用 send() 方法的技巧在于构造要发送的 Message 对象。在这个 "Hello World" 样例中，通过给定字符串的字节数组
     * 来构建 Message 实例。对于 String 值来说，这足够了，但是如果消息的负载是复杂对象的话，那它就会复杂得多。
     *
     * 鉴于这种情况，就有了 convertAndSend() 方法，它会自动将对象转换为 Message。它需要一个消息转换器的帮助来完成
     * 该任务，默认的消息转换器是 SimpleMessageConverter，它适用于 String、Serializable 实例以及字节数组。Spring
     * AMQP 还提供了其他几个有用的消息转换器，其中包括使用 JSON 和 XML 数据的消息转换器。
     *
     * 现在，已经发送了消息，后续将转向回话的另外一端，看一下如何获取消息。
     */
    public static void main(String[] args) {

    }

}
