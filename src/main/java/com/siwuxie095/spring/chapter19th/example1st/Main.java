package com.siwuxie095.spring.chapter19th.example1st;

/**
 * @author Jiajing Li
 * @date 2021-03-29 07:57:14
 */
public class Main {

    /**
     * 使用 Spring 发送 Email
     *
     * 毫无疑问，Email 已经成为常见的通信形式，取代了很多传统的通信方式，如邮政邮件、电话，在一定程度上也替代了面对面的交流。
     * Email能够提供与异步消息相同的收益，只不过发送者和接收者都是实际的人而已。只要你在邮件客户端上点击 "发送" 按钮，就可
     * 以转移到其他的任务中了，因为知道接收者最终将会收到并阅读（希望如此）你的 Email。
     *
     * 但是，Email 的发送者不一定是实际的人。有时候，Email 消息是由应用程序发送给用户的。有可能是电子商务网站上的订单确认
     * 邮件，也有可能是银行账户某项交易的自动提醒。不管邮件的主题是什么，都可能需要开发发送 Email 消息的应用程序。幸好，在
     * 这个方面，Spring 会提供帮助。
     *
     * 之前借助 Spring 对消息功能的支持，以排队任务的形式异步发送 Spittle 提醒给 Spittr 的其他用户。但是，这项任务并未完
     * 成，因为没有发送 Email 消息。现在，将会完成这项任务，在这里首先会看一下 Spring 是如何抽象邮件发送这一问题的，然后利
     * 用这一抽象发送包含 Spittle 提醒的 Email 消息。
     */
    public static void main(String[] args) {

    }

}
