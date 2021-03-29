package com.siwuxie095.spring.chapter19th.example2nd;

import com.icegreen.greenmail.spring.GreenMailBean;
import com.siwuxie095.spring.chapter19th.example2nd.cfg.MailConfig;
import com.siwuxie095.spring.chapter19th.example2nd.domain.Spitter;
import com.siwuxie095.spring.chapter19th.example2nd.domain.Spittle;
import com.siwuxie095.spring.chapter19th.example2nd.email.SpitterMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Jiajing Li
 * @date 2021-03-29 08:33:22
 */
@SuppressWarnings("all")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MailConfig.class)
public class SpitterMailServiceImplTest {

    @Autowired
    private SpitterMailService mailService;

    @Autowired
    private GreenMailBean mailServer;

    @Test
    public void sendSimpleSpittleEmail() throws Exception {
        Spitter spitter = new Spitter(1L, "habuma", null, "Craig Walls", "c@habuma.com", true);
        Spittle spittle = new Spittle(1L, spitter, "Hiya!", new Date());
        mailService.sendSimpleSpittleEmail("craig@habuma.com", spittle);

        MimeMessage[] receivedMessages = mailServer.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals("New spittle from Craig Walls", receivedMessages[0].getSubject());
        assertEquals("Craig Walls says: Hiya!", ((String) receivedMessages[0].getContent()).trim());
        Address[] from = receivedMessages[0].getFrom();
        assertEquals(1, from.length);
        assertEquals("noreply@spitter.com", ((InternetAddress) from[0]).getAddress());
        assertEquals("craig@habuma.com", ((InternetAddress) receivedMessages[0].getRecipients(Message.RecipientType.TO)[0]).getAddress());
    }

}
