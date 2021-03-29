package com.siwuxie095.spring.chapter19th.example2nd.email;

import com.siwuxie095.spring.chapter19th.example2nd.domain.Spittle;

import javax.mail.MessagingException;

/**
 * @author Jiajing Li
 * @date 2021-03-29 08:27:57
 */
@SuppressWarnings("all")
public interface SpitterMailService {

    void sendSimpleSpittleEmail(String to, Spittle spittle);

    void sendSpittleEmailWithAttachment(String to, Spittle spittle)
            throws MessagingException;

}
