package com.siwuxie095.spring.chapter18th.example6th.marcopolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * @author Jiajing Li
 * @date 2021-03-28 15:29:04
 */
@SuppressWarnings("all")
@Controller
public class MarcoController {

    private static final Logger logger = LoggerFactory
            .getLogger(MarcoController.class);

    @MessageMapping("/marco")
    public Shout handleShout(Shout incoming) {
        logger.info("Received message: " + incoming.getMessage());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        Shout outgoing = new Shout();
        outgoing.setMessage("Polo!");

        return outgoing;
    }

}

