package com.siwuxie095.spring.chapter20th.example4th;

import org.springframework.stereotype.Component;

import javax.management.Notification;
import javax.management.NotificationListener;

/**
 * @author Jiajing Li
 * @date 2021-04-01 22:10:54
 */
@SuppressWarnings("all")
@Component
public class PagingNotificationListener
        implements NotificationListener {

    @Override
    public void handleNotification(
            Notification notification, Object handback) {
        // ...
    }

}
