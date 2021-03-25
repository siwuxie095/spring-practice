package com.siwuxie095.spring.chapter17th.example12th;

/**
 * @author Jiajing Li
 * @date 2021-03-25 21:42:35
 */
public class Main {

    /**
     * 配置 Spring 支持 AMQP 消息
     *
     * 当第一次使用 Spring JMS 抽象的时候，首先配置了一个连接工厂。与之类似，使用 Spring AMQP 前也要配置一个连接工厂。
     * 只不过，所要配置的不是 JMS 的连接工厂，而是需要配置 AMQP 的连接工厂。更具体来讲，需要配置 RabbitMQ 连接工厂。
     *
     * 什么是 RabbitMQ？
     *
     * RabbitMQ 是一个流行的开源消息代理，它实现了 AMQP。Spring AMQP 为 RabbitMQ 提供了支持，包括 RabbitMQ 连接工
     * 厂、模板以及 Spring 配置命名空间。
     *
     * 在使用它发送和接收消息之前，你需要预先安装 RabbitMQ。可以在 www.rabbitmq.com/download.html 上找到安装指南。
     * 根据你所运行的 OS 不同，这会有所差别，所以根据环境的不同，遵循相应指南进行安装即可。
     *
     * 配置 RabbitMQ 连接工厂最简单的方式就是使用 Spring AMQP 所提供的 rabbit 配置命名空间。为了使用这项功能，需要
     * 确保在 Spring 配置文件中已经声明了该模式：
     *
     * <?xml version="1.0" encoding="UTF-8"?>
     * <beans:beans xmlns:beans="http://www.springframework.org/schema/beans"
     *              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     *              xmlns="http://www.springframework.org/schema/rabbit"
     *              xsi:schemaLocation="http://www.springframework.org/schema/rabbit
     *              http://www.springframework.org/schema/rabbit/spring-rabbit-1.0.xsd
     *               http://www.springframework.org/schema/beans
     *               http://www.springframework.org/schema/beans/spring-beans-4.0.xsd">
     *
     * </beans:beans>
     *
     * 尽管不是必须的，但这里还是选择在这个配置中将 rabbit 作为首选的命名空间，将 beans 作为第二位的命名空间。这是因为
     * 在这个配置中，会更多的声明 rabbit 而不是 bean，这样的话，只会有少量的 bean 元素使用 "beans:" 前缀，而 rabbit
     * 元素就能够避免使用前缀了。
     *
     * rabbit 命名空间包含了多个在 Spring 中配置 RabbitMQ 的元素。但此时，你最感兴趣的可能就是 <connection-factory>。
     * 按照其最简单的形式，可以在配置 RabbitMQ 连接工厂的时候没有任何属性：
     *
     * <connection-factory />
     *
     * 这的确能够运行起来，但是所导致的结果就是连接工厂 bean 没有可用的 bean ID，这样的话就难将连接工厂装配到需要它的
     * bean 中。因此，可能希望通过 id 属性为其设置一个 bean ID：
     *
     * <connection-factory id="connectionFactory" />
     *
     * 默认情况下，连接工厂会假设 RabbitMQ 服务器监听 localhost 的 5672 端口，并且用户名和密码均为 guest。对于开发
     * 来讲，这是合理的默认值，但是对于生产环境，可能希望修改这些默认值。如下 <connection-factory> 的设置重写了默认
     * 的做法：
     *
     * <connection-factory id="connectionFactory"
     *      host="${rabbitmq.host}"
     *      port="${rabbitmq.port}"
     *      username="${rabbitmq.username}"
     *      password="${rabbitmq.password}" />
     *
     * 这里使用占位符来指定值，这样配置项可以在 Spring 配置文件之外进行管理（很可能位于属性文件中）。
     *
     * 除了连接工厂以外，还要考虑使用其他的几个配置元素。接下来，看一下如何创建队列、Exchange 以及 binding。
     *
     *
     *
     * 声明队列、Exchange 以及 binding
     *
     * 在 JMS 中，队列和主题的路由行为都是通过规范建立的，AMQP 与之不同，它的路由更加丰富和灵活，依赖于如何定义队列和
     * Exchange 以及如何将它们绑定在一起。声明队列、Exchange 和 binding 的一种方式是使用 RabbitMQ Channel 接口
     * 的各种方法。但是直接使用 RabbitMQ 的 Channel 接口非常麻烦。Spring AMQP 能否帮助声明消息路由组件呢？
     *
     * 幸好，rabbit 命名空间包含了多个元素，帮助声明队列、Exchange 以及将它们结合在一起的 binding。如下列出了这些元
     * 素。
     * （1）<queue>：创建一个队列
     * （2）<fanout-exchange>：创建一个 fanout 类型的 Exchange
     * （3）<header-exchange>：创建一个 header 类型的 Exchange
     * （4）<topic-exchange>：创建一个 topic 类型的 Exchange
     * （5）<direct-exchange>：创建一个 direct 类型的 Exchange
     * （6）<bindings><binding/></bindings>：元素定义一个或多个元素的集合。元素创建 Exchange 和队列之间的 binding
     *
     * PS：Spring AMQP 的 rabbit 命名空间包含了多个元素，用来创建队列、Exchange 以及将它们结合在一起的 binding。
     *
     * 这些配置元素要与 <admin> 元素一起使用。<admin> 元素会创建一个 RabbitMQ 管理组件（administrative component），
     * 它会自动创建（如果它们在 RabbitMQ 代理中尚未存在的话）上述这些元素所声明的队列、Exchange 以及 binding。
     *
     * 例如，如果你希望声明名为 spittle.alert.queue 的队列，只需要在 Spring 配置中添加如下的两个元素即可：
     *
     * <admin connection-factory="connectionFactory" />
     * <queue id="spittleAlertQueue" name="spittle.alert.queue" />
     *
     * 对于简单的消息来说，只需做这些就足够了。这是因为默认会有一个没有名称的 direct Exchange，所有的队列都会绑定到这
     * 个 Exchange 上，并且 routing key 与队列的名称相同。在这个简单的配置中，可以将消息发送到这个没有名称的 Exchange
     * 上，并将 routing key 设定为 spittle.alert.queue，这样消息就会路由到这个队列中。实际上，这里重新创建了 JMS
     * 的点对点模型。
     *
     * 但是，更加有意思的路由需要声明一个或更多的 Exchange，并将其绑定到队列上。例如，如果要将消息路由到多个队列中，而
     * 不管 routing key 是什么，可以按照如下的方式配置一个 fanout 以及多个队列：
     *
     * <admin connection-factory="connectionFactory" />
     * <queue id="spittleAlertQueue" name="spittle.alert.queue.1" />
     * <queue id="spittleAlertQueue" name="spittle.alert.queue.2" />
     * <queue id="spittleAlertQueue" name="spittle.alert.queue.3" />
     * <fanout-exchange name="spittle.fanout">
     *     <bindings>
     *         <binding queue="spittle.alert.queue.1" />
     *         <binding queue="spittle.alert.queue.2" />
     *         <binding queue="spittle.alert.queue.3" />
     *     </bindings>
     * </fanout-exchange>
     *
     * 借助 rabbit 命名空间中的元素，会有无数种在 RabbitMQ 配置路由的方式。
     */
    public static void main(String[] args) {

    }

}
