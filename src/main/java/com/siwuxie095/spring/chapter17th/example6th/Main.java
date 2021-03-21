package com.siwuxie095.spring.chapter17th.example6th;

/**
 * @author Jiajing Li
 * @date 2021-03-21 22:10:43
 */
public class Main {

    /**
     * 在 Spring 中搭建消息代理
     *
     * ActiveMQ 是一个伟大的开源消息代理产品，也是使用 JMS 进行异步消息传递的最佳选择。在开始使用 ActiveMQ 之前，需要从
     * http://activemq.apache.org 下载二进制发行包。下载完 ActiveMQ 后，将其解压缩到本地硬盘中。在解压目录中，会找到
     * 文件 activemq-core-5.16.1.jar（截止 2021/03/21 为止的最新版本）。为了能够使用 ActiveMQ 的 API，需要将此 JAR
     * 文件添加到应用程序的类路径中。
     *
     * 在 bin 目录下，可以看到为各种操作系统所创建的对应子目录。在这些子目录下，可以找到用于启动 ActiveMQ 的脚本。例如，
     * 要在 OS X 下启动 ActiveMQ，只需要在 "bin/macosx" 目录下运行 activemq start。运行脚本后，ActiveMQ 就准备好了，
     * 这时可以使用它作为消息代理。
     *
     *
     *
     * 1、创建连接工厂
     *
     * 在这里，将了解如何采用不同的方式在 Spring 中使用 JMS 发送和接收消息。在所有的示例中，都需要借助 JMS 连接工厂通过
     * 消息代理发送消息。因为选择了 ActiveMQ 作为消息代理，所以必须配置 JMS 连接工厂，让它知道如何连接到 ActiveMQ。
     * ActiveMQConnectionFactory 是 ActiveMQ 自带的连接工厂，在 Spring 中可以使用如下方式进行配置：
     *
     * <bean id="connectionFactory" class="org.apache.activemq.spring.ActiveMQConnectionFactory" />
     *
     * 默认情况下，ActiveMQConnectionFactory 会假设 ActiveMQ 代理监听 localhost 的 61616 端口。对于开发环境来说，
     * 这没有什么问题，但是在生产环境下，ActiveMQ 可能会在不同的主机和端口上。如果是这样的话，可以使用 brokerURL 属性
     * 来指定代理的 URL：
     *
     * <bean id="connectionFactory"
     *      class="org.apache.activemq.spring.ActiveMQConnectionFactory"
     *      p:brokerURL="tcp://localhost:61616" />
     *
     * 配置连接工厂还有另外一种方式，既然知道正在与 ActiveMQ 打交道，那就可以使用 ActiveMQ 自己的 Spring 配置命名空
     * 间来声明连接工厂（适用于 ActiveMQ 4.1 之后的所有版本）。首先，必须确保在 Spring 的配置文件中声明了 amq 命名
     * 空间，然后就可以使用 <amq:connectionFactory> 元素声明连接工厂：
     *
     * <amp:connectionFactory id="connectionFactory" brokerURL="tcp://localhost:61616" />
     *
     * 注意，<amq:connectionFactory> 元素很明显是为 ActiveMQ 所准备的。如果使用不同的消息代理实现，它们不一定会提供
     * Spring 配置命名空间。如果没有提供的话，那就需要使用 <bean> 来装配连接工厂。
     *
     * 后续会多次使用 connectionFactory bean，但是现在，只需要通过配置 brokerURL 属性来告知连接工厂消息代理的位置就
     * 足够了。在本例中，brokerURL 属性中的 URL 指定连接工厂要连接到本地机器的 61616 端口（这个端口是 ActiveMQ 监听
     * 的默认端口）上的 ActiveMQ。
     *
     *
     *
     * 2、声明 ActiveMQ 消息目的地
     *
     * 除了连接工厂外，还需要消息传递的目的地。目的地可以是一个队列，也可以是一个主题，这取决于应用的需求。
     *
     * 不论使用的是队列还是主题，都必须使用特定的消息代理实现类在 Spring 中配置目的地 bean。例如，下面的 <bean> 声明
     * 定义了一个 ActiveMQ 队列：
     *
     * <bean id="queue"
     *      class="org.apache.activemq.command.ActiveMQQueue"
     *      c:_0="spitter.queue" />
     *
     * 同样，下面的 <bean> 声明定义了一个 ActiveMQ 主题：
     *
     * <bean id="topic"
     *      class="org.apache.activemq.command.ActiveMQTopic"
     *      c:_0="spitter.topic" />
     *
     * 在第一个示例中，构造器指定了队列的名称，这样消息代理就能获知该信息，而在接下来示例中，名称则为 spitter.topic。
     *
     * 与连接工厂相似的是，ActiveMQ 命名空间提供了另一种方式来声明队列和主题。对于队列，可以使用 <amq:queue> 元素来
     * 声明：
     *
     * <amq:queue id="spittleQueue" physicalName="spittle.alert.queue" />
     *
     * 如果是主题，可以使用 <amq:topic> 元素来声明：
     *
     * <amq:topic id="spittleTopic" physicalName="spittle.alert.topic" />
     *
     * 不管是哪种类型，都是借助 physicalName 属性指定消息通道的名称。
     *
     * 到此为止，已经看到了如何声明使用 JMS 所需的组件。现在已经准备好发送和接收消息了。为此，将使用 Spring 的
     * JmsTemplate —— Spring 对 JMS 支持的核心部分。但是首先，先看看如果没有 JmsTemplate，JMS 是怎样使用的，
     * 以此了解 JmsTemplate 到底提供了些什么。后续将会对此进行介绍。
     */
    public static void main(String[] args) {

    }

}
