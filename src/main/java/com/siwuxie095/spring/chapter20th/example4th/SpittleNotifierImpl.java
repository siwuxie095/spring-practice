package com.siwuxie095.spring.chapter20th.example4th;

import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;

import javax.management.Notification;

/**
 * @author Jiajing Li
 * @date 2021-04-01 21:56:28
 */
@SuppressWarnings("all")
@Component
@ManagedResource("spitter:name=SpitterNotifier")
@ManagedNotification(
        notificationTypes = "SpittleNotifier.OneMillionSpittles",
        name = "TODO")
public class SpittleNotifierImpl
        implements NotificationPublisherAware, SpittleNotifier {

    private NotificationPublisher notificationPublisher;

    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }


    @Override
    public void millionthSpittlePosted() {
        notificationPublisher.sendNotification(
                new Notification("SpittleNotifier.OneMillionSpittles",
                        this, 0));
    }

}
