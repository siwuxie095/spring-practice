package com.siwuxie095.spring.chapter19th.example2nd;

/**
 * @author Jiajing Li
 * @date 2021-03-29 08:09:47
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 配置 Spring 发送邮件
     *
     * Spring Email 抽象的核心是 MailSender 接口。顾名思义，MailSender 的实现能够通过连接 Email 服务器实现
     * 邮件发送的功能。
     *
     * PS：Spring 的 MailSender 接口是 Spring Email 抽象 API 的核心组件。它把 Email 发送给邮件服务器，由
     * 服务器进行邮件投递。
     *
     * Spring 自带了一个 MailSender 的实现也就是 JavaMailSenderImpl，它会使用 JavaMail API 来发送 Email。
     * Spring 应用在发送 Email 之前，必须要将 JavaMailSenderImpl 装配为 Spring 应用上下文中的一个 bean。
     *
     *
     *
     * 1、配置邮件发送器
     *
     * 按照最简单的形式，只需在 @Bean 方法中使用几行代码就能将 JavaMailSenderImpl 配置为一个 bean：
     *
     *     @Bean
     *     public JavaMailSenderImpl mailSender(Environment env) {
     *         JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
     *         mailSender.setHost(env.getProperty("mailserver.host"));
     *         return mailSender;
     *     }
     *
     * 属性 host 是可选的（它默认是底层 JavaMail 会话的主机），但你可能希望设置该属性。它指定了要用来发送 Email
     * 的邮件服务器主机名。按照这里的配置，会从注入的 Environment 中获取值，这样就能在 Spring 之外管理邮件服务器
     * 的配置（比如在属性文件中）。
     *
     * 默认情况下，JavaMailSenderImpl 假设邮件服务器监听 25 端口（标准的 SMTP 端口）。如果你的邮件服务器监听
     * 不同的端口，那么可以使用 port 属性指定正确的端口号。例如：
     *
     *     @Bean
     *     public JavaMailSenderImpl mailSender(Environment env) {
     *         JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
     *         mailSender.setHost(env.getProperty("mailserver.host"));
     *         mailSender.setPort(Integer.parseInt(env.getProperty("mailserver.port")));
     *         return mailSender;
     *     }
     *
     * 类似地，如果邮件服务器需要认证的话，你还需要设置 username 和 password 属性：
     *
     *     @Bean
     *     public JavaMailSenderImpl mailSender(Environment env) {
     *         JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
     *         mailSender.setHost(env.getProperty("mailserver.host"));
     *         mailSender.setPort(Integer.parseInt(env.getProperty("mailserver.port")));
     *         mailSender.setUsername(env.getProperty("mailserver.username"));
     *         mailSender.setPassword(env.getProperty("mailserver.password"));
     *         return mailSender;
     *     }
     *
     * 到目前为止，JavaMailSenderImpl 已经配置完成，它可以创建自己的邮件会话，但是你可能已经在 JNDI 中配置了
     * javax.mail.MailSession（也可能是你的应用服务器放在那里的）。如果这样的话，那就没有必要为
     * JavaMailSenderImpl 配置详细的服务器细节了。可以配置它使用 JNDI 中已就绪的 MailSession。
     *
     * 借助 JndiObjectFactoryBean，可以在如下的 @Bean 方法中配置一个 bean，它会从 JNDI 中查找 MailSession：
     *
     *     @Bean
     *     public JndiObjectFactoryBean mailSession() {
     *         JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
     *         jndi.setJndiName("mail/Session");
     *         jndi.setProxyInterface(Session.class);
     *         jndi.setResourceRef(true);
     *         return jndi;
     *     }
     *
     * 已经看到过如何使用 Spring 的 <jee:jndi-lookup> 元素从 JNDI 中获取对象，这里可以使用 <jee:jndi-lookup>
     * 来创建一个 bean，它引用了 JNDI 中的邮件会话：
     *
     * <jee:jndi-lookup id="mailSession" jndi-name="mail/Session" resource-ref="true" />
     *
     * 邮件会话准备就绪之后，现在可以将其装配到 mailSender bean 中了：
     *
     *     @Bean
     *     public MailSender mailSender(Session session) {
     *         JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
     *         mailSender.setSession(session);
     *         return mailSender;
     *     }
     *
     * 通过将邮件会话装配到 JavaMailSenderImpl 的 session 属性中，已经完全替换了原来的服务器（以及用户名/密码）
     * 配置。现在邮件会话完全通过 JNDI 进行配置和管理。JavaMailSenderImpl 能够专注于发送邮件而不必自己处理邮件
     * 服务器了。
     *
     *
     *
     * 2、装配和使用邮件发送器
     *
     * 邮件发送器已经配置完成，现在需要将其装配到使用它的 bean 中了。在 Spittr 应用程序中，最适合发送 Email 的是
     * SpitterEmailServiceImpl 类。这个类有一个 mailSender 属性，它使用了 @Autowired 注解：
     *
     * @Autowired
     * JavaMailSender mailSender;
     *
     * 当 Spring 将 SpitterEmailServiceImpl 创建为一个 bean 的时候，它将查找实现了 MailSender 的 bean，这
     * 样的 bean 可以装配到 mailSender 属性中。它将会找到在前边配置的 mailSender bean 并使用它。mailSender
     * bean 装配完成后，就可以构建和发送 Email 了。
     *
     * 这里想要给 Spitter 用户发送 Email 提示他的朋友写了新的 Spittle，所以需要一个方法来发送 Email，这个方法
     * 要接受 Email 地址和 Spittle 对象信息。如下的 sendSimpleSpittleEmail() 方法使用邮件发送器完成了该功能：
     *
     *     @Override
     *     public void sendSimpleSpittleEmail(String to, Spittle spittle) {
     *         SimpleMailMessage message = new SimpleMailMessage();
     *         String spitterName = spittle.getSpitter().getFullName();
     *         message.setFrom("noreply@spitter.com");
     *         message.setTo(to);
     *         message.setSubject("New spittle from " + spitterName);
     *         message.setText(spitterName + " says: " + spittle.getText());
     *         mailSender.send(message);
     *     }
     *
     * sendSimpleSpittleEmail() 方法所做的第一件事就是构造 SimpleMailMessage 实例。正如其名称所示，这个对象
     * 可以很便捷地发送 Email 消息。
     *
     * 接下来，将设置消息的细节。通过邮件消息的 setFrom() 和 setTo() 方法指定了 Email 的发送者和接收者。在通过
     * setSubject() 方法设置完主题后，虚拟的 "信封" 已经完成了。剩下的就是调用 setText() 方法来设置消息的内容。
     *
     * 最后一步是将消息传递给邮件发送器的 send() 方法，这样邮件就发送出去了。
     *
     * 现在，已经配置好了邮件发送器并使用它来发送简单的 Email 消息。可以看到，使用 Spring 的 Email 抽象非常简单。
     * 后续将更进一步，看一下如何添加附件并创建丰富内容的 Email 消息。
     */
    public static void main(String[] args) {

    }

}
