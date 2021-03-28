package com.siwuxie095.spring.chapter18th.example7th;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

/**
 * @author Jiajing Li
 * @date 2021-03-28 16:44:58
 */
@SuppressWarnings("all")
@Service
public class SpittleFeedServiceImpl implements SpittleFeedService {

    private SimpMessageSendingOperations messaging;

    @Autowired
    public SpittleFeedServiceImpl(SimpMessageSendingOperations messaging) {
        this.messaging = messaging;
    }

    @Override
    public void broadcastSpittle(Spittle spittle) {
        messaging.convertAndSend("/topic/spittefeed", spittle);
    }
}
