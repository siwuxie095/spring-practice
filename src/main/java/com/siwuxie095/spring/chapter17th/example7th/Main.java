package com.siwuxie095.spring.chapter17th.example7th;

/**
 * @author Jiajing Li
 * @date 2021-03-21 23:10:53
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用 Spring 的 JMS 模板
     *
     * 正如所看到的，JMS 为 Java 开发者提供了与消息代理进行交互来发送和接收消息的标准 API，而且几乎每个消息代理
     * 实现都支持 JMS，因此不必因为使用不同的消息代理而学习私有的消息 API。
     *
     * 虽然 JMS 为所有的消息代理提供了统一的接口，但是这种接口用起来并不是很方便。使用 JMS 发送和接收消息并不像
     * 拿一张邮票并贴在信封上那么简单。下面将要看到，JMS 还要求为邮递车加油（只是比喻的说法）。
     *
     *
     *
     * 1、处理失控的 JMS 代码
     *
     * 众所周知，传统的 JDBC 代码在处理连接、语句、结果集和异常时是多么冗长和繁杂。遗憾的是，传统的 JMS 使用了
     * 类似的编程模型，如下所示。
     *
     *     public void sendMessage() {
     *         ConnectionFactory cf =
     *                 new ActiveMQConnectionFactory("tcp://localhost:61616");
     *         Connection conn = null;
     *         Session session = null;
     *         try {
     *             conn = cf.createConnection();
     *             session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
     *             Destination destination = new ActiveMQQueue("spitter.queue");
     *             MessageProducer producer = session.createProducer(destination);
     *             TextMessage message = session.createTextMessage();
     *             message.setText("Hello World!");
     *             producer.send(message);
     *         } catch (JMSException e) {
     *             // handle exception
     *         } finally {
     *             try {
     *                 if (session != null) {
     *                     session.close();
     *                 }
     *                 if (conn != null) {
     *                     conn.close();
     *                 }
     *             } catch (JMSException e) {
     *                 // ...
     *             }
     *         }
     *     }
     *
     * 再次声明这是一段失控的代码！就像 JDBC 示例一样，差不多使用了 20 行代码，只是为了发送一条 "Hello world!"
     * 消息。实际上，其中只有几行代码是用来发送消息的，剩下的代码仅仅是为了发送消息而进行的设置。
     *
     * 接收端也没有好到哪里去，如下所示。
     *
     *     public void recieveMessage() {
     *         ConnectionFactory cf =
     *                 new ActiveMQConnectionFactory("tcp://localhost:61616");
     *         Connection conn = null;
     *         Session session = null;
     *         try {
     *             conn = cf.createConnection();
     *             conn.start();
     *             session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
     *             Destination destination = new ActiveMQQueue("spitter.queue");
     *             MessageConsumer consumer = session.createConsumer(destination);
     *             Message message = consumer.receive();
     *             TextMessage textMessage = (TextMessage) message;
     *             System.out.println("GOT A MESSAGE: " + textMessage.getText());
     *             conn.start();
     *         } catch (JMSException e) {
     *             // handle exception
     *         } finally {
     *             try {
     *                 if (session != null) {
     *                     session.close();
     *                 }
     *                 if (conn != null) {
     *                     conn.close();
     *                 }
     *             } catch (JMSException e) {
     *                 // ...
     *             }
     *         }
     *     }
     *
     * 同样，这也是用一大段代码来实现如此简单的事情。如果逐行地比较，会发现它们几乎是完全一样的。如果查看上千个其
     * 他的 JMS 例子，会发现它们也是很相似的。只不过，其中一些会从 JNDI 中获取连接工厂，而另一些则是使用主题代
     * 替队列。但是无论如何，它们都大致遵循相同的模式。
     *
     * 因为这些样板式代码，每次使用 JMS 时都要不断地做很多重复工作。更糟糕的是，你会发现在重复编写其他开发者的
     * JMS 代码。
     *
     * 已经看到了 Spring 的 JdbcTemplate 是如何处理失控的 JDBC 样板式代码的。下面来介绍一下 Spring 的
     * JmsTemplate 如何对 JMS 的样板式代码实现相同的功能。
     *
     *
     *
     * 2、使用 JMS 模版
     *
     * 针对如何消除冗长和重复的 JMS 代码，Spring 给出的解决方案是 JmsTemplate。JmsTemplate 可以创建连接、获
     * 得会话以及发送和接收消息。这使得开发者可以专注于构建要发送的消息或者处理接收到的消息。
     *
     * 另外，JmsTemplate 可以处理所有抛出的笨拙的 JMSException 异常。如果在使用 JmsTemplate 时抛出
     * JMSException 异常，JmsTemplate 将捕获该异常，然后抛出一个非检查型异常，该异常是 Spring 自带的
     * JmsException 异常的子类。如下列出了标准的 JMSException 异常与 Spring 的非检查型异常之间的映
     * 射关系（下面 Spring 指 org.springframework.jms.*，标准 JMS 指 javax.jms.*）。
     * （1）
     * Spring：DestinationResolutionException
     * 标准 JMS：Spring 特有的 —— 当 Spring 无法解析目的地名称时抛出
     * （2）
     * Spring：IllegalStateException
     * 标准 JMS：IllegalStateException
     * （3）
     * Spring：InvalidClientIDException
     * 标准 JMS：InvalidClientIDException
     * （4）
     * Spring：InvalidDestinationException
     * 标准 JMS：InvalidSelectorException
     * （5）
     * Spring：InvalidSelectorException
     * 标准 JMS：InvalidSelectorException
     * （6）
     * Spring：JmsSecurityException
     * 标准 JMS：JmsSecurityException
     * （7）
     * Spring：ListenerExecutionFailedException
     * 标准 JMS：Spring 特有的 —— 当监听器方法执行失败时抛出
     * （8）
     * Spring：MessageConversionException
     * 标准 JMS：Spring 特有的 —— 当消息转换失败时抛出
     * （9）
     * Spring：MessageEOFException
     * 标准 JMS：MessageEOFException
     * （10）
     * Spring：MessageFormatException
     * 标准 JMS：MessageFormatException
     * （11）
     * Spring：MessageNotReadableException
     * 标准 JMS：MessageNotReadableException
     * （12）
     * Spring：MessageNotWriteableException
     * 标准 JMS：MessageNotWriteableException
     * （13）
     * Spring：ResourceAllocationException
     * 标准 JMS：ResourceAllocationException
     * （14）
     * Spring：SynchedLocalTransactionFailedException
     * 标准 JMS：Spring 特有的 —— 当同步的本地事务不能完成时抛出
     * （15）
     * Spring：TransactionInprogressException
     * 标准 JMS：TransactionInprogressException
     * （16）
     * Spring：TransactionRolledBackException
     * 标准 JMS：TransactionRolledBackException
     * （17）
     * Spring：UncategorizedJmsException
     * 标准 JMS：Spring 特有的 —— 当没有其他异常适用时抛出
     *
     * PS：Spring 的 JmsTemplate 会捕获标准的 JMSException 异常，再以 Spring 的非检查型异常 JmsException
     * 子类重新抛出。
     *
     * 对于 JMS API 来说，JMSException 的确提供了丰富且具有描述性的子类集合，能够更清楚地知道发生了什么错误。
     * 不过，所有的 JMSException 异常的子类都是检查型异常，因此必须要捕获。JmsTemplate 捕获这些异常，并重新
     * 抛出对应非检查型 JMSException 异常的子类。
     *
     * 为了使用 JmsTemplate，需要在 Spring 的配置文件中将它声明为一个 bean。如下的 XML 可以完成这项工作：
     *
     * <bean id="jmsTemplate"
     *      class="org.springframework.jms.core.JmsTemplate"
     *      c:_0-ref="connectionFactory" />
     *
     * 因为 JmsTemplate 需要知道如何连接到消息代理，所以必须为 connectionFactory 属性设置实现了 JMS 的
     * ConnectionFactory 接口的 bean 引用。
     *
     * 这就是配置 JmsTemplate 所需要做的所有工作 —— 现在 JmsTemplate 已经准备好了。下面开始发送消息吧！
     *
     *
     *
     * 3、发送消息
     *
     * 在想建立的 Spittr 应用程序中，其中有一个特性就是当创建 Spittle 的时候提醒其他用户（或许是通过 E-mail）。
     * 可以在增加 Spittle 的地方直接实现该特性。但是搞清楚发送提醒给谁以及实际发送这些提醒可能需要一段时间，这
     * 会影响到应用的性能。当增加一个新的 Spittle 时，希望应用是敏捷的，能够快速做出响应。
     *
     * 与其在增加 Spittle 时浪费时间发送这些信息，不如对该项工作进行排队，在响应返回给用户之后再处理它。与直接
     * 发送消息给其他用户所花费的时间相比，发送消息给队列或主题所花费的时间是微不足道的。
     *
     * 为了在 Spittle 创建的时候异步发送 spittle 提醒，这里为 Spittr 应用引入 AlertService：
     *
     * public interface AlertService {
     *
     *     void sendSpittleAlert(Spittle spittle);
     *
     * }
     *
     * 正如所看到的，AlertService 是一个接口，只定义了一个操作 —— sendSpittleAlert()。
     *
     * 而 AlertServiceImpl 实现了 AlertService 接口，它使用 JmsOperation（JmsTemplate 所实现的接口）将
     * Spittle 对象发送给消息队列，而队列会在稍后得到处理。
     *
     * public class AlertServiceImpl implements AlertService {
     *
     *     private JmsOperations jmsOperations;
     *
     *     public AlertServiceImpl(JmsOperations jmsOperations) {
     *         this.jmsOperations = jmsOperations;
     *     }
     *
     *     public void sendSpittleAlert(final Spittle spittle) {
     *         jmsOperations.send(
     *                 "spittle.alert.queue",
     *                 new MessageCreator() {
     *                     public Message createMessage(Session session)
     *                             throws JMSException {
     *                         return session.createObjectMessage(spittle);
     *                     }
     *                 });
     *     }
     *
     * }
     *
     * JmsOperations 的 send() 方法的第一个参数是 JMS 目的地名称，标识消息将发送给谁。当调用 send() 方法时，
     * JmsTemplate 将负责获得 JMS 连接、会话并代表发送者发送消息。
     *
     * PS：JmsTemplate 代表发送者来负责处理发送消息的复杂过程。
     *
     * 使用 MessageCreator（在这里的实现是作为一个匿名内部类）来构造消息。在 MessageCreator 的
     * createMessage() 方法中，通过 session 创建了一个对象消息：传入一个 Spittle 对象，返回一
     * 个对象消息。
     *
     * 就是这么简单！注意，sendSpittleAlert() 方法专注于组装和发送消息。在这里没有连接或会话管理的代码，
     * JmsTemplate 帮助处理了所有的相关事项，而且也不需要捕获 JMSException 异常。JmsTemplate 将捕获
     * 抛出的所有 JMSException 异常，然后重新抛出某一种非检查型异常。
     *
     *
     *
     * 4、设置默认目的地
     *
     * 在上面的代码中，明确指定了一个目的地，在 send() 方法中将 Spittle 消息发向此目的地。当希望通过程序选择一
     * 个目的地时，这种形式的 send() 方法很适用。但是在 AlertServiceImpl 案例中，总是将 Spittle 消息发给相
     * 同的目的地，所以这种形式的 send() 方法并不能带来明显的好处。
     *
     * 与其每次发送消息时都指定一个目的地，不如为 JmsTemplate 装配一个默认的目的地：
     *
     *     <bean id="jmsTemplate"
     *           class="org.springframework.jms.core.JmsTemplate"
     *           c:_0-ref="connectionFactory"
     *           p:defaultDestinationName="spittle.alert.queue" />
     *
     * 在这里，将目的地的名称设置为 spittle.alert.queue，但它只是一个名称：它并没有说明你所处理的目的地是什么
     * 类型。如果已经存在该名称的队列或主题的话，就会使用已有的。如果尚未存在的话，将会创建一个新的目的地（通常
     * 会是队列）。但是，如果你想指定要创建的目的地类型的话，那么你可以将之前创建的队列或主题的目的地 bean 装配
     * 进来：
     *
     *     <bean id="jmsTemplate"
     *           class="org.springframework.jms.core.JmsTemplate"
     *           c:_0-ref="connectionFactory"
     *           p:defaultDestination-ref="spittleTopic" />
     *
     * 现在，调用 JmsTemplate 的 send() 方法时，可以去除第一个参数了：
     *
     *         jmsOperations.send(
     *                 new MessageCreator() {
     *                     public Message createMessage(Session session)
     *                             throws JMSException {
     *                         return session.createObjectMessage(spittle);
     *                     }
     *                 });
     *
     * 这种形式的 send() 方法只需要传入一个 MessageCreator。因为希望消息发送给默认目的地，所以没有必要再指定
     * 特定的目的地。
     *
     * 在调用 send() 方法时，不必再显式指定目的地能够让任务得以简化。但是如果使用消息转换器的话，发送消息会更加
     * 简单。
     *
     *
     *
     * 5、在发送时，对消息进行转换
     *
     * 除了 send() 方法，JmsTemplate 还提供了 convertAndSend() 方法。与 send() 方法不同，
     * convertAndSend() 方法并不需要 MessageCreator 作为参数。这是因为 convertAndSend()
     * 会使用内置的消息转换器（message converter）创建消息。
     *
     * 当使用 convertAndSend() 时，sendSpittleAlert() 可以减少到方法体中只包含一行代码：
     *
     *     public void sendSpittleAlert(Spittle spittle) {
     *         jmsOperations.convertAndSend(spittle);
     *     }
     *
     * 就像变魔术一样，Spittle 会在发送之前转换为 Message。不过就像所有的魔术一样，JmsTemplate 内部会进行一
     * 些处理。它使用一个 MessageConverter 的实现类将对象转换为 Message。
     *
     * MessageConverter 是 Spring 定义的接口，只有两个需要实现的方法：
     *
     * public interface MessageConverter {
     *     Message toMessage(Object object, Session session)
     *     throws JMSException, MessageConversionException;
     *
     *     Object fromMessage(Message message)
     *     throws JMSException, MessageConversionException;
     * }
     *
     * 尽管这个接口实现起来很简单，但通常并没有必要创建自定义的实现。Spring 已经提供了多个实现，如下所示。
     * （1）MappingJacksonMessageConverter：使用 Jackson JSON 库实现消息与 JSON 格式之间的相互转换。
     * （2）MappingJackson2MessageConverter：使用 Jackson 2 JSON 库实现消息与 JSON 格式之间的相互转换。
     * （3）MarshallingMessageConverter：使用 JAXB 库实现消息与 XML 格式之间的相互转换。
     * （4）SimpleMessageConverter：实现 String 与 TextMessage 之间的相互转换，字节数组与 BytesMessage
     * 之间的相互转换，Map 与 MapMessage 之间的相互转换以及 Serializable 对象与 ObjectMessage 之间的相互
     * 转换。
     *
     * PS：Spring 为通用的转换任务提供了多个消息转换器（所有的消息转换器都位于 org.springframework.jms.
     * support.converter 包中）。
     *
     * 默认情况下，JmsTemplate 在 convertAndSend() 方法中会使用 SimpleMessageConverter。但是通过将消息
     * 转换器声明为 bean 并将其注入到 JmsTemplate 的 messageConverter 属性中，可以重写这种行为。例如，如
     * 果你想使用 JSON 消息的话，那么可以声明一个 MappingJacksonMessageConverter bean：
     *
     *     <bean id="messageConverter"
     *           class="org.springframework.jms.support.converter.
     *           MappingJacksonMessageConverter" />
     *
     * 然后，可以将其注入到 JmsTemplate 中，如下所示：
     *
     *     <bean id="jmsTemplate"
     *           class="org.springframework.jms.core.JmsTemplate"
     *           c:_0-ref="connectionFactory"
     *           p:defaultDestinationName="spittle.alert.queue"
     *           p:messageConverter-ref="messageConverter" />
     *
     * 各个消息转换器可能会有额外的配置，进而实现转换过程的细粒度控制。例如，MappingJacksonMessageConverter
     * 能够配置转码以及自定义 Jackson ObjectMapper。可以查阅每个消息转换器的 JavaDoc 以了解如何更加细粒度地
     * 配置它们。
     *
     *
     *
     * 6、接收消息
     *
     * 现在已经了解了如何使用 JmsTemplate 发送消息。但如果是接收端，那要怎么办呢？JmsTemplate 是不是也可以
     * 接收消息呢？
     *
     * 没错，的确可以。事实上，使用 JmsTemplate 接收消息甚至更简单，只需要调用 JmsTemplate 的 receive()
     * 方法即可，如下所示。
     *
     * public Spittle receiveSpittleAlert() {
     *     try {
     *         ObjectMessage receivedMessage = (ObjectMessage) jmsOperations.receive();
     *         return (Spittle) receivedMessage.getObject();
     *     } catch (JMSException jmsException) {
     *         throw JmsUtils.convertJmsAcessException(jmsException);
     *     }
     * }
     *
     * 当调用 JmsTemplate 的 receive() 方法时，JmsTemplate 会尝试从消息代理中获取一个消息。如果没有可用的
     * 消息，receive() 方法会一直等待，直到获得消息为止。
     *
     * 因为知道 Spittle 消息是作为一个对象消息来发送的，所以它可以在到达后转型为 ObjectMessage。然后，调用
     * getObject() 方法把 ObjectMessage 转换为 Spittle 对象并返回此对象。
     *
     * 但是这里存在一个问题，不得不对可能抛出的 JMSException 进行处理。正如已经提到的，JmsTemplate 可以很好
     * 地处理抛出的 JmsException 检查型异常，然后把异常转换为 Spring 非检查型异常 JmsException 并重新抛出。
     * 但是它只对调用 JmsTemplate 的方法时才适用。JmsTemplate 无法处理调用 ObjectMessage 的 getObject()
     * 方法时所抛出的 JMSException 异常。
     *
     * 因此，要么捕获 JMSException 异常，要么声明本方法抛出 JMSException 异常。为了遵循 Spring 规避检查型
     * 异常的设计理念，不建议本方法抛出 JMSException 异常，所以选择捕获该异常。在 catch 代码块中，使用 Spring
     * 中 JmsUtils 的 convertJmsAccessException() 方法把检查型异常 JMSException 转换为非检查型异常
     * JmsException。这其实是在其他场景中由 JmsTemplate 做的事情。
     *
     * 在 receiveSpittleAlert() 方法中，可以改善的一点就是使用消息转换器。在 convertAndSend() 中，已经看
     * 到了如何将对象转换为 Message。不过，它们还可以用在接收端，也就是使用 JmsTemplate 的
     * receiveAndConvert()：
     *
     *     public Spittle retrieveSpittleAlert() {
     *         return (Spittle) jmsOperations.receiveAndConvert();
     *     }
     *
     * 现在，没有必要将 Message 转换为 ObjectMessage，也没有必要通过调用 getObject() 来获取 Spittle，更
     * 无需担心检查型的 JMSException 异常。这个新的 retrieveSpittleAlert() 简洁了许多。但是，依然还有一
     * 个很小且不容易察觉的问题。
     *
     * 使用 JmsTemplate 接收消息的最大缺点在于 receive() 和 receiveAndConvert() 方法都是同步的。这意味着
     * 接收者必须耐心等待消息的到来，因此这些方法会一直被阻塞，直到有可用消息（或者直到超时）。同步接收异步发送
     * 的消息，是不是感觉很怪异？
     *
     * 这就是消息驱动 POJO 的用武之处。后续将会介绍如何使用能够响应消息的组件异步接收消息，而不是一直等待消息的
     * 到来。
     */
    public static void main(String[] args) {

    }

}
