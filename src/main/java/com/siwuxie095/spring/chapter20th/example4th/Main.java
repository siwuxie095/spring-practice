package com.siwuxie095.spring.chapter20th.example4th;

/**
 * @author Jiajing Li
 * @date 2021-04-01 21:46:53
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 处理通知
     *
     * 通过查询 MBean 获得信息只是查看应用状态的一种方法。但当应用发生重要事件时，如果希望能够及时告知开发者，这通常不是
     * 最有效的方法。
     *
     * 例如，假设 Spittr 应用保存了已发布的 Spittle 数量，而开发者希望知道每发布一百万 Spittle 时的精确时间（例如一百
     * 万、两百万、三百万等）。一种解决方法是编写代码定期查询数据库，计算 Spittle 的数量。但是执行这种查询会让应用和数据
     * 库都很繁忙，因为它需要不断的检查 Spittle 的数量。
     *
     * 与重复查询数据库获得 Spittle 的数量相比，更好的方式是当这类事件发生时让 MBean 来通知开发者。JMX 通知（JMX
     * notification）是 MBean 与外部世界主动通信的一种方法，而不是等待外部应用对 MBean 进行查询以获得信息。
     *
     * Spring 通过 NotificationPublisherAware 接口提供了发送通知的支持。任何希望发送通知的 MBean 都必须实现这个接口。
     * 例如，如下代码中的 SpittleNotifierImpl。
     *
     * @Component
     * @ManagedResource("spitter:name=SpitterNotifier")
     * @ManagedNotification(
     *         notificationTypes = "SpittleNotifier.OneMillionSpittles",
     *         name = "TODO")
     * public class SpittleNotifierImpl
     *         implements NotificationPublisherAware, SpittleNotifier {
     *
     *     private NotificationPublisher notificationPublisher;
     *
     *     @Override
     *     public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
     *         this.notificationPublisher = notificationPublisher;
     *     }
     *
     *
     *     @Override
     *     public void millionthSpittlePosted() {
     *         notificationPublisher.sendNotification(
     *                 new Notification("SpittleNotifier.OneMillionSpittles",
     *                         this, 0));
     *     }
     *
     * }
     *
     * 正如所看到的，SpittleNotifierImpl 实现了 NotificationPublisherAware 接口。这并不是一个要求苛刻的接口，它仅
     * 要求实现一个方法：setNotificationPublisher。
     *
     * SpittleNotificationImpl 也实现了 SpittleNotifier 接口的方法：millionthSpittlePosted()。这个方法使用了
     * setNotificationPublisher() 方法所注入的 NotificationPublisher 来发送通知：Spittle 数量又到了一个新的百
     * 万级别。
     *
     * 一旦 sendNotification() 方法被调用，就会发出通知。嗯 ... 好像这里还没决定谁来接收这个通知。那就来建立一个通知
     * 监听器来监听和处理通知。
     *
     *
     *
     * 监听通知
     *
     * 接收 MBean 通知的标准方法是实现 javax.management.NotificationListener 接口。例如，考虑一下
     * PagingNotificationListener：
     *
     * @Component
     * public class PagingNotificationListener
     *         implements NotificationListener {
     *
     *     @Override
     *     public void handleNotification(
     *             Notification notification, Object handback) {
     *         // ...
     *     }
     *
     * }
     *
     * PagingNotificationListener 是一个典型的 JMX 通知监听器。当接收到通知时，将会调用 handleNotification() 方
     * 法处理通知。大概的逻辑可能是，PagingNotificationListener 的 handleNotification() 方法将向寻呼机或手机上发
     * 送消息来告知 Spittle 数量又到了一个新的百万级别。
     *
     * 剩下的工作只需要使用 MBeanExporter 注册 PagingNotificationListener：
     *
     *     @Bean
     *     public MBeanExporter mbeanExporter() {
     *         MBeanExporter exporter = new MBeanExporter();
     *         Map<String, NotificationListener> mappings = new HashMap<>();
     *         mappings.put("Spitter:name=PagingNotificationListener",
     *                 new PagingNotificationListener());
     *         exporter.setNotificationListenerMappings(mappings);
     *         return exporter;
     *     }
     *
     * MBeanExporter 的 notificationListenerMappings 属性用于在监听器和监听器所希望监听的 MBean 之间建立映射。在
     * 本示例中，建立了 PagingNotificationListener 来监听由 SpittleNotifier MBean 所发布的通知。
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    public static void main(String[] args) {

    }

}
