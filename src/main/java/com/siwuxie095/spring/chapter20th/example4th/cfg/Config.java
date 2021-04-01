package com.siwuxie095.spring.chapter20th.example4th.cfg;

import com.siwuxie095.spring.chapter20th.example4th.PagingNotificationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;

import javax.management.NotificationListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jiajing Li
 * @date 2021-04-01 22:13:47
 */
@SuppressWarnings("all")
@Configuration
public class Config {

    @Bean
    public MBeanExporter mbeanExporter() {
        MBeanExporter exporter = new MBeanExporter();
        Map<String, NotificationListener> mappings = new HashMap<>();
        mappings.put("Spitter:name=PagingNotificationListener",
                new PagingNotificationListener());
        exporter.setNotificationListenerMappings(mappings);
        return exporter;
    }

}
