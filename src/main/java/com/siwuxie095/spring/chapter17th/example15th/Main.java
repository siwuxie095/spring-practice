package com.siwuxie095.spring.chapter17th.example15th;

/**
 * @author Jiajing Li
 * @date 2021-03-26 21:34:07
 */
public class Main {

    /**
     * 小结
     *
     * 异步消息通信与同步 RPC 相比有几个优点。间接通信带来了应用之间的松散耦合，因此减轻了其中任意一个应用崩溃
     * 所带来的影响。此外，因为消息转发给了收件人，因此发送者不必等待响应。在很多情况下，这会提高应用的性能。
     *
     * 虽然 JMS 为所有的 Java 应用程序提供了异步通信的标准 API，但是它使用起来很繁琐。Spring 消除了 JMS 样
     * 板式代码和异常捕获代码，让异步消息通信更易于使用。
     *
     * 在这里，了解了 Spring 通过消息代理和 JMS 建立应用程序之间异步通信的几种方式。Spring 的 JMS模板消除了
     * 传统的 JMS 编程模型所必需的样板式代码，而基于 Spring 的消息驱动 bean 可以通过声明 bean 的方法允许方
     * 法响应来自于队列或主题中的消息。同样了解了如何通过 Spring 的 JMS invoker 为 Spring bean 提供基于消
     * 息的 RPC。
     *
     * 在这里，已经看到了如何在应用程序之间使用异步通信。后续将会延续这一话题，了解如何借助 WebSocket 在基于浏
     * 览器的客户端和服务器之间实现异步通信。
     */
    public static void main(String[] args) {

    }

}
