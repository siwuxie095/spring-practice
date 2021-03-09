package com.siwuxie095.spring.chapter15th.example3rd.cfg;

import com.siwuxie095.spring.chapter15th.example3rd.service.SpitterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

/**
 * @author Jiajing Li
 * @date 2021-03-09 22:39:44
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter15th.example3rd.*"})
public class RmiConfig {

    @Bean
    public RmiServiceExporter rmiExporter(SpitterService spitterService) {
        RmiServiceExporter rmiExporter = new RmiServiceExporter();
        rmiExporter.setService(spitterService);
        rmiExporter.setServiceName("SpitterService");
        rmiExporter.setServiceInterface(SpitterService.class);
        //rmiExporter.setRegistryHost("rmi.spitter.com");
        //rmiExporter.setRegistryPort(1199);
        return rmiExporter;
    }

}

