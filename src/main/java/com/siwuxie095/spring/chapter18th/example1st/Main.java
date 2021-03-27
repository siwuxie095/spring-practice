package com.siwuxie095.spring.chapter18th.example1st;

/**
 * @author Jiajing Li
 * @date 2021-03-27 15:01:27
 */
public class Main {

    /**
     * 使用 WebSocket 和 STOMP 实现消息功能
     *
     * 之前看到了如何使用 JMS 和 AMQP 在应用程序之间发送消息。异步消息是应用程序之间通用的交流方式。但是，
     * 如果某一应用是运行在 Web 浏览器中，那就需要一些稍微不同的技巧了。
     *
     * WebSocket 协议提供了通过一个套接字实现全双工通信的功能。除了其他的功能之外，它能够实现 Web 浏览器
     * 和服务器之间的异步通信。全双工意味着服务器可以发送消息给浏览器，浏览器也可以发送消息给服务器。
     *
     * Spring 4.0 为 WebSocket 通信提供了支持，包括：
     * （1）发送和接收消息的低层级 API；
     * （2）发送和接收消息的高级 API；
     * （3）用来发送消息的模板；
     * （4）支持 SockJS，用来解决浏览器端、服务器以及代理不支持 WebSocket 的问题。
     *
     * 在这里，将会学习借助 Spring 的 WebSocket 功能实现服务器端和基于浏览器的应用之间实现异步通信。首先
     * 会从如何使用 Spring 的低层级 WebSocket API 开始。
     */
    public static void main(String[] args) {

    }

}
