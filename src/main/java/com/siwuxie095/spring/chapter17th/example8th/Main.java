package com.siwuxie095.spring.chapter17th.example8th;

/**
 * @author Jiajing Li
 * @date 2021-03-23 21:18:23
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 创建消息驱动的 POJO
     *
     * JmsTemplate 的 receive() 方法被调用时，JmsTemplate 会查看队列或主题中是否有消息，直到收到消息或者
     * 等待超时才会返回。这期间，应用无法处理任何事情，只能等待是否有消息。如果应用能够继续进行其他业务处理，
     * 当消息到达时再去通知它，不是更好吗？
     *
     * EJB2 规范的一个重要内容是引入了消息驱动 bean（message-driven bean，MDB）。MDB 是可以异步处理消息的
     * EJB。换句话说，MDB 将 JMS 目的地中的消息作为事件，并对这些事件进行响应。而与之相反的是，同步消息接收者
     * 在消息可用前会一直处于阻塞状态。
     *
     * MDB 是 EJB 中的一个亮点。即使那些狂热的 EJB 反对者也认为 MDB 可以优雅地处理消息。EJB2 MDB 的唯一缺点
     * 是它们必须要实现 java.ejb.MessageDrivenBean。此外，它们还必须实现一些 EJB 生命周期的回调方法。简而
     * 言之，EJB 2 MDB 不是纯的 POJO。
     *
     * 在 EJB3 规范中，MDB 进一步简化了，使其更像 POJO。不再需要实现 MessageDrivenBean 接口，而是实现更通用
     * 的 javax.jms.MessageListener 接口，并使用 @MessageDriven 注解标注 MDB。
     *
     * Spring 2.0 提供了它自己的消息驱动 bean 来满足异步接收消息的需求，这种形式与 EJB3 的 MDB 很相似。在这里，
     * 将学习到 Spring 是如何使用消息驱动 POJO（这里将其简称为 MDP）来支持异步接收消息的。
     *
     *
     *
     * 1、创建消息监听器
     *
     * 如果使用 EJB 的消息驱动模型来创建 Spittle 的提醒处理器，需要使用 @MessageDriven 注解进行标注。即使
     * 它不是严格要求的，但 EJB 规范还是建议 MDB 实现 MessageListener 接口。Spittle 的提醒处理器最终可能
     * 是这样的：
     *
     * @MessageDriven(mappedName = "jms/spittle.alert.queue")
     * public class SpittleAlertHandler implements MessageListener {
     *
     *     @Resource
     *     private MessageDrivenContext mdc;
     *
     *     @Override
     *     public void onMessage(Message message) {
     *          ...
     *     }
     *
     * }
     *
     * 想象一下，如果消息驱动组件不需要实现 MessageListener 接口，世界将是多么的简单。在这里，天是蔚蓝的，鸟
     * 儿唱着喜欢的歌，不再需要实现 onMessage() 方法或者注入 MessgeDrivenContext。
     *
     * 好吧，可能 EJB3 规范所要求的 MDB 也算不上太麻烦。但是事实上，SpittleAlertHandler 的 EJB3 实现太依
     * 赖于 EJB 的消息驱动 API，并不是所希望的 POJO。理想情况下，希望提醒处理器能够处理消息，但是不用编码，就
     * 好像它知道应该做什么。
     *
     * Spring 提供了以 POJO 的方式处理消息的能力，这些消息来自于 JMS 的队列或主题中。例如，基于 POJO 实现
     * SpittleAlertHandler 就足以做到这一点。
     *
     * public class SpittleAlertHandler {
     *
     *      public void handleSpittleAlert(Spittle spittle) {
     *          // ... implemetation goes here ...
     *      }
     *
     * }
     *
     * 虽然改变天空的颜色和训练鸟儿歌唱超出了 Spring 的范围，但这段代码所展示的现实与描绘的理想世界非常接近。
     * 稍后会编写 handleSpittleAlert() 方法的具体内容。现在，这段代码所展示的 SpittleAlertHandler 没有
     * 任何 JMS 的痕迹。从任意一个角度观察，它都是一个纯粹的 POJO。它仍然可以像 EJB 那样处理消息，只不过它
     * 还需要一些 Spring 的配置。
     *
     *
     *
     * 2、配置消息监听器
     *
     * 为 POJO 赋予消息接收能力的诀窍是在 Spring 中把它配置为消息监听器。Spring 的 jms 命名空间提供了所需要
     * 的一切。首先，先把处理器声明为 bean：
     *
     * <bean id="spittleHandler" class="com.habuma.spittr.alerts.SpittleAlertHandler" />
     *
     * <jms:listener-container connection-factory="connectionFactory">
     *     <jms:listener destination="spitter.alert.queue"
     *     ref="spittleHandler" method="handleSpittleAlert" />
     * </jms:listener-container>
     *
     * 在这里，在消息监听器容器中包含了一个消息监听器。消息监听器容器（message listener container）是一个特
     * 殊的 bean，它可以监控 JMS 目的地并等待消息到达。一旦有消息到达，它取出消息，然后把消息传给任意一个对此
     * 消息感兴趣的消息监听器。
     *
     * 为了在 Spring 中配置消息监听器容器和消息监听器，使用了 Spring jms 命名空间中的两个元素。
     * <jms:listener-container> 中包含了 <jms:listener> 元素。这里的 connection-factory
     * 属性配置了对 connectionFactory 的引用，容器中的每个 <jms:listener> 都使用这个连接工厂
     * 进行消息监听。在本示例中，connection-factory 属性可以移除，因为该属性的默认值就是
     * connectionFactory。
     *
     * 对于 <jms:listener> 元素，它用于标识一个 bean 和一个可以处理消息的方法。为了处理 Spittle 提醒消息，
     * ref 元素引用了 spittleHandler bean。当消息到达 spitter.alert.queue 队列（通过 destination 属
     * 性配置）时，spittleHandler bean 的 handleSpittleAlert() 方法（通过 method 属性指定的）会被触发。
     *
     * 值得一提的是，如果 ref 属性所标示的 bean 实现了 MessageListener，那就没有必要再指定 method 属性了，
     * 默认就会调用 onMessage() 方法。
     */
    public static void main(String[] args) {

    }

}
