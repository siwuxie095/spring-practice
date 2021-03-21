package com.siwuxie095.spring.chapter17th.example5th;

/**
 * @author Jiajing Li
 * @date 2021-03-21 17:46:23
 */
public class Main {

    /**
     * 使用 JMS 发送消息
     *
     * Java 消息服务（Java Message Service，JMS）是一个 Java 标准，定义了使用消息代理的通用 API。
     * 在 JMS 出现之前，每个消息代理都有私有的 API，这就使得不同代理之间的消息代码很难通用。但是借助
     * JMS，所有遵从规范的实现都使用通用的接口，这就类似于 JDBC 为数据库操作提供了通用的接口一样。
     *
     * Spring 通过基于模板的抽象为 JMS 功能提供了支持，这个模板也就是 JmsTemplate。使用 JmsTemplate，
     * 能够非常容易地在消息生产方发送队列和主题消息，在消费消息的那一方，也能够非常容易地接收这些消息。
     * Spring 还提供了消息驱动 POJO 的理念：这是一个简单的 Java 对象，它能够以异步的方式响应队列或
     * 主题上到达的消息。
     *
     * 这里将会讨论 Spring 对 JMS 的支持，包括 JmsTemplate 和消息驱动 POJO。但是在发送和接收消息
     * 之前，首先需要一个消息代理，它能够在消息的生产者和消费者之间传递消息。对 Spring JMS 的探索就
     * 从在 Spring 中搭建消息代理开始吧。后续将会对如何搭建消息代理进行介绍。
     */
    public static void main(String[] args) {

    }

}
