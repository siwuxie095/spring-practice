package com.siwuxie095.spring.chapter19th.example2nd.cfg;

import com.icegreen.greenmail.spring.GreenMailBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Session;
import java.util.Arrays;

/**
 * @author Jiajing Li
 * @date 2021-03-29 08:25:54
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan("com.siwuxie095.spring.chapter19th.example2nd")
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Bean
    public GreenMailBean greenMail() {
        GreenMailBean greenMailBean = new GreenMailBean();
        greenMailBean.setUsers(Arrays.asList("app:letmein01@spitter.com"));
        return greenMailBean;
    }

    @Bean
    public JavaMailSenderImpl mailSender(Environment env) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("mailserver.host"));
        mailSender.setPort(Integer.parseInt(env.getProperty("mailserver.port")));
        mailSender.setUsername(env.getProperty("mailserver.username"));
        mailSender.setPassword(env.getProperty("mailserver.password"));
        return mailSender;
    }

    //@Bean
    //public MailSender mailSender(Session session) {
    //    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    //    mailSender.setSession(session);
    //    return mailSender;
    //}
    //
    //@Bean
    //public JndiObjectFactoryBean mailSession() {
    //    JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
    //    jndi.setJndiName("mail/Session");
    //    jndi.setProxyInterface(Session.class);
    //    jndi.setResourceRef(true);
    //    return jndi;
    //}

}

