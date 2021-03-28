package com.siwuxie095.spring.chapter18th.example8th.spittr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Date;

/**
 * @author Jiajing Li
 * @date 2021-03-28 17:07:44
 */
@SuppressWarnings("all")
@Controller
public class SpittrMessageController {

    private SpittleRepository spittleRepo;
    private SpittleFeedService feedService;

    @Autowired
    public SpittrMessageController(SpittleRepository spittleRepo, SpittleFeedService feedService) {
        this.spittleRepo = spittleRepo;
        this.feedService = feedService;
    }

    @MessageMapping("/spittle")
    @SendToUser("/queue/notifications")
    public Notification handleSpittle(Principal principal, SpittleForm form) {
        Spittle spittle = new Spittle(principal.getName(), form.getText(), new Date());
        spittleRepo.save(spittle);
        feedService.broadcastSpittle(spittle);
        return new Notification("Saved Spittle for user: " + principal.getName());
    }

}

