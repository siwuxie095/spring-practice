package com.siwuxie095.spring.chapter17th.example10th;

/**
 * @author Jiajing Li
 * @date 2021-03-24 21:36:08
 */
public class Main {

    /**
     * 使用 AMQP 实现消息功能
     *
     * 你可能会疑惑为什么还需要另外一个消息规范。难道 JMS 还不够好吗？AMQP 提供了哪些 JMS 所不具备的特性呢？
     *
     * 实际上，AMQP 具有多项 JMS 所不具备的优势。首先，AMQP 为消息定义了线路层（wire-level protocol）的
     * 协议，而 JMS 所定义的是 API 规范。JMS 的 API 协议能够确保所有的实现都能通过通用的 API 来使用，但是
     * 并不能保证某个 JMS 实现所发送的消息能够被另外不同的 JMS 实现所使用。而 AMQP 的线路层协议规范了消息的
     * 格式，消息在生产者和消费者间传送的时候会遵循这个格式。这样 AMQP 在互相协作方面就要优于 JMS —— 它不仅
     * 能跨不同的 AMQP 实现，还能跨语言和平台。
     *
     * 相比 JMS，AMQP 另外一个明显的优势在于它具有更加灵活和透明的消息模型。使用 JMS 的话，只有两种消息模型
     * 可供选择：点对点和发布-订阅。这两种模型在 AMQP 当然都是可以实现的，但 AMQP 还能够以其他的多种方式来
     * 发送消息，这是通过将消息的生产者与存放消息的队列解耦实现的。
     *
     * Spring AMQP 是 Spring 框架的扩展，它能够在 Spring 应用中使用 AMQP 风格的消息。后续会看到，Spring
     * AMQP 提供了一个 API，借助这个 API，能够以非常类似于 Spring JMS 抽象的形式来使用 AMQP。这意味着，之
     * 前所学习的 JMS 内容能够帮助你理解如何使用 Spring AMQP 来发送和接收消息。
     */
    public static void main(String[] args) {

    }

}
