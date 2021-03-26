package com.siwuxie095.spring.chapter17th.example14th;

/**
 * @author Jiajing Li
 * @date 2021-03-26 08:45:58
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 接收 AMQP 消息
     *
     * JMS 提供了两种从队列中获取信息的方式：使用 JmsTemplate 的同步方式以及使用消息驱动 POJO 的异步方式。
     * Spring AMQP 提供了类似的方式来获取通过 AMQP 发送的消息。因为已经有了 RabbitTemplate，所以首先看
     * 一下如何使用它同步地从队列中获取消息。
     *
     *
     *
     * 1、使用 RabbitTemplate 来接收消息
     *
     * RabbitTemplate 提供了多个接收信息的方法。最简单就是 receive() 方法，它位于消息的消费者端，对应于
     * RabbitTemplate 的 send() 方法。借助 receive() 方法，可以从队列中获取一个 Message 对象：
     *
     * Message message = rabbit.receive("spittle.alert.queue");
     *
     * 或者，如果愿意的话，你还可以配置获取消息的默认队列，这是通过在配置模板的时候，设置 queue 属性实现的：
     *
     * <template id="rabbitTemplate"
     *          connection-factory="connectionFactory"
     *          exchange="spittle.alert.exchange"
     *          routing-key="spittle.alerts"
     *          queue="spittle.alert.queue" />
     *
     * 这样的话，我们在调用 receive() 方法的时候，不需要设置任何参数就能从默认队列中获取消息了：
     *
     * Message message = rabbit.receive();
     *
     * 在获取到 Message 对象之后，可能需要将它 body 属性中的字节数组转换为想要的对象。就像在发送的时候将领
     * 域对象转换为 Message 一样，将接收到的 Message 转换为领域对象同样非常繁琐。因此，可以考虑使用
     * RabbitTemplate 的 receiveAndConvert() 方法作为替代方案：
     *
     * Spittle spittle = (Spittle) rabbit.receiveAndConvert("spittle.alert.queue");
     *
     * 还可以省略调用参数中的队列名称，这样它就会使用模板的默认队列名称：
     *
     * Spittle spittle = (Spittle) rabbit.receiveAndConvert();
     *
     * receiveAndConvert() 方法会使用与 sendAndConvert() 方法相同的消息转换器，将 Message 对象转换为
     * 原始的类型。
     *
     * 调用 receive() 和 receiveAndConvert() 方法都会立即返回，如果队列中没有等待的消息时，将会得到 null。
     * 这就需要管理轮询（polling）以及必要的线程，实现队列的监控。
     *
     * 并非必须同步轮询并等待消息到达，Spring AMQP 还提供了消息驱动 POJO 的支持，这和 Spring JMS 是相同
     * 的特性。下面看一下如何通过消息驱动 AMQP POJO 的方式来接收消息。
     *
     *
     *
     * 2、定义消息驱动的 AMQP POJO
     *
     * 如果你想在消息驱动 POJO 中异步地消费使用 Spittle 对象，首先要解决的问题就是这个 POJO 本身。如下的
     * SpittleAlertHandler 扮演了这个角色：
     *
     * public class SpittleAlertHandler {
     *
     *     public void handleSpittleAlert(Spittle spittle) {
     *          // ... implementation goes here ...
     *     }
     *
     * }
     *
     * 注意，这个类与借助 JMS 消费 Spittle 时所用到 SpittleAlertHandler 完全一致。之所以能够重用相同的
     * POJO 是因为这个类丝毫没有依赖于 JMS 或 AMQP，并且不管通过什么机制传递过来 Spittle 对象，它都能够
     * 进行处理。
     *
     * 还需要在 Spring 应用上下文中将 SpittleAlertHandler 声明为一个 bean：
     *
     * <bean id="spittleListener" class="com.habuma.spittr.alert.SpittleAlertHandler" />
     *
     * 同样，在使用基于 JMS 的 MDP 时，已经做过相同的事情，没有什么丝毫的差异。
     *
     * 最后，需要声明一个监听器容器和监听器，当消息到达的时候，能够调用 SpittleAlertHandler。在基于 JMS
     * 的 MDP 中，做过相同的事情，但是基于 AMQP 的 MDP 在配置上有一个细微的差别：
     *
     * <listener-container connection-factory="connectionFactory">
     *     <listener ref="spittleListener"
     *          method="handleSpittleAlert"
     *          queue-names="spittle.alert.queue" />
     * </listener-container>
     *
     * 这里 <listener-container> 与 <listener> 都与 JMS 对应的元素非常类似。但是，这些元素来自 rabbit
     * 命名空间，而不是 JMS 命名空间。
     *
     * 还有一个细微的差别，这里不再通过 destination 属性（JMS 中的做法）来监听队列或主题，这里通过
     * queue-names 属性来指定要监听的队列。但是，除此之外，基于 AMQP 的 MDP 与基于 JMS 的 MDP 都
     * 非常类似。
     *
     * 你可能也意识到了，queue-names 属性的名称使用了复数形式。在这里只设定了一个要监听的队列，但是允许设置
     * 多个队列的名称，用逗号分割即可。
     *
     * 另外一种指定要监听队列的方法是引用 <queue> 元素所声明的队列 bean。可以通过 queues 属性来进行设置：
     *
     * <listener-container connection-factory="connectionFactory">
     *     <listener ref="spittleListener"
     *          method="handleSpittleAlert"
     *          queues="spittleAlertQueue" />
     * </listener-container>
     *
     * 同样，这里可以接受逗号分割的 queue ID 列表。当然，这需要在声明队列的时候，为其指定 ID。例如，如下是
     * 重新定义的提醒队列，这次指定了 ID：
     *
     * <queue id="spittleAlertQueue" name="spittle.alert.queue" />
     *
     * 注意，这里的 id 属性用来在 Spring 应用上下文中设置队列的 bean ID， 而 name 属性指定了 RabbitMQ
     * 代理中队列的名称。
     */
    public static void main(String[] args) {

    }

}
