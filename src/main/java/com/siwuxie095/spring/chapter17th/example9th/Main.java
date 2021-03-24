package com.siwuxie095.spring.chapter17th.example9th;

/**
 * @author Jiajing Li
 * @date 2021-03-24 08:05:45
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 使用基于消息的 RPC
     *
     * 之前展示了 Spring 把 bean 的方法暴露为远程服务以及从客户端向这些服务发起调用的几种方式。后来学习了如何通过
     * 队列和主题在应用程序之间发送消息。现在将了解一下如何使用 JMS 作为传输通道来进行远程调用。
     *
     * 为了支持基于消息的 RPC，Spring 提供了 JmsInvokerServiceExporter，它可以把 bean 导出为基于消息的服务；
     * 为客户端提供了 JmsInvokerProxyFactoryBean 来使用这些服务。
     *
     * 之前已经看到，Spring 提供了多种方式把 bean 导出为远程服务。使用 RmiServiceExporter 把 bean 导出为 RMI
     * 服务，使用 HessianExporter 和 BurlapExporter 导出为基于 HTTP 的 Hessian 和 Burlap 服务，还使用
     * HttpInvokerServiceExporter 创建基于 HTTP 的 HTTP invoker 服务。 但 Spring 还提供了一种之前未探讨的
     * 服务导出器。
     *
     *
     *
     * 1、导出基于 JMS 的服务
     *
     * JmsInvokerServiceExporter 很类似于其他的服务导出器。事实上，JmsInvokerServiceExporter 与
     * HttpInvokerServiceExporter 在名称上有某种对称型。如果 HttpInvokerServiceExporter 可以导
     * 出基于 HTTP 通信的服务，那么 JmsInvokerServiceExporter 就应该可以导出基于 JMS 的服务。
     *
     * 为了演示 JmsInvokerServiceExporter 是如何工作的，考虑如下的 AlertServiceImpl。
     *
     * @Component("alertService")
     * public AlertServiceImpl implements AlertService {
     *
     *     private JavaMailSender mailSender;
     *     private String alertEmailAddress;
     *
     *     public AlertServiceImpl(JavaMailSender mailSender,
     *                              String alertEmailAddress) {
     *         this.mailSender = mailSender;
     *         this.alertEmailAddress = alertEmailAddress;
     *     }
     *
     *     public void sendSpittleAlert(final Spittle spittle) {
     *         SimpleMailMessage message = new SimpleMailMessage();
     *         String spitterName = spittle.getSpitter().getFullName();
     *         message.setFrom("noreply@spitter.com");
     *         message.setTo(alertEmailAddress);
     *         message.setSubject("New spittle from " + spitterName);
     *         message.setText(spitterName = " says: " + spittle.getText());
     *         mailSender.send(message);
     *     }
     *
     * }
     *
     * 现在不要过于关注 sendSpittleAlert() 方法的细节。后续将会继续探讨如何使用 Spring 发送 E-mail。现在，需要
     * 关注的重点在于 AlertServiceImpl 是一个简单的 POJO，没有任何迹象标示它要用来处理 JMS 消息。它只是实现了简
     * 单的 AlertService 接口，该接口如下所示：
     *
     * public interface AlertService {
     *     void sendSpittleAlert(Spittle spittle);
     * }
     *
     * 正如所看到的，AlertServiceImpl 使用了 @Component 注解来标注，所以它会被 Spring 自动发现并注册为 Spring
     * 应用上下文中 ID 为 alertService 的 bean。在配置 JmsInvokerServiceExporter 时，将引用这个 bean：
     *
     * <bean id="alertServiceExporter"
     *      class="org.springframework.jms.remoting.JmsInvokerServiceExporter"
     *      p:service-ref="alertService"
     *      p:serviceInterface="com.habuma.spittr.alert.AlertService" />
     *
     * 这个 bean 的属性描述了导出的服务应该是什么样子的。service 属性设置为 alertService bean 的引用，它是远程
     * 服务的实现。同时，serviceInterface 属性设置为远程服务对外提供接口的全限定类名。
     *
     * 导出器的属性并没有描述服务如何基于 JMS 通信的细节。但好消息是 JmsInvokerServiceExporter 可以充当 JMS
     * 监听器。因此，使用 <jms:listener-container> 元素配置它：
     *
     * <jms:listener-container connection-factory="connectionFactory">
     *     <jms:listener destination="spitter.alert.queue"
     *                   ref="alertServiceExporter" />
     * </jms:listener-container>
     *
     * 这里为 JMS 监听器容器指定了连接工厂，所以它能够知道如何连接消息代理，而 <jms:listener> 声明指定了远程消息
     * 的目的地。
     *
     *
     *
     * 2、使用基于 JMS 的服务
     *
     * 这时候，基于 JMS 的提醒服务已经准备好了，等待队列中名字为 spitter.alert.queue 的 RPC 消息到达。在客户端，
     * JmsInvokerProxyFactoryBean 用来访问服务。
     *
     * JmsInvokerProxyFactoryBean 很类似于之前所讨论的其他远程代理工厂 bean。它隐藏了访问远程服务的细节，并提供
     * 一个易用的接口，通过该接口客户端与远程服务进行交互。与代理 RMI 服务或 HTTP 服务的最大区别在于，
     * JmsInvokerProxyFactoryBean 代理了通过 JmsInvokerServiceExporter 所导出的 JMS 服务。
     *
     * 为了使用提醒服务，可以像下面那样配置 JmsInvokerProxyFactoryBean：
     *
     * <bean id="alertService"
     *      class="org.springframework.jms.remoting.JmsInvokerProxyFactoryBean"
     *      p:connectionFactory-ref="connectionFactory"
     *      p:queueName="spittle.alert.queue"
     *      p:serviceInterface="com.habuma.spittr.alert.AlertService" />
     *
     * connectionFactory 和 queryName 属性指定了 RPC 消息如何被投递 —— 在这里，也就是在给定的连接工厂中，所配置
     * 的消息代理里面名为 spitter.alert.queue 的队列。对于 serviceInterface，指定了代理应该通过 AlertService
     * 接口暴露功能。
     *
     * 多年来，JMS 一直是 Java 应用中主流的消息解决方案。但是对于 Java 和 Spring 开发者来说，JMS 并不是唯一的消息
     * 可选方案。在过去的几年中，高级消息队列协议（Advanced Message Queuing Protocol，AMQP）得到了广泛的关注。
     * 因此，Spring 也为通过 AMQP 发送消息提供了支持，后续将会对其进行介绍。
     */
    public static void main(String[] args) {

    }

}
