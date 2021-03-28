package com.siwuxie095.spring.chapter18th.example6th.marcopolo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Jiajing Li
 * @date 2021-03-28 15:31:30
 */
@SuppressWarnings("all")
@Component
public class RandomNumberMessageSender {

    private SimpMessagingTemplate messaging;

    @Autowired
    public RandomNumberMessageSender(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    @Scheduled(fixedRate = 10000)
    public void sendRandomNumber() {
        Shout random = new Shout();
        random.setMessage("Random # : " + (Math.random() * 100));
        messaging.convertAndSend("/topic/marco", random);
    }

}

