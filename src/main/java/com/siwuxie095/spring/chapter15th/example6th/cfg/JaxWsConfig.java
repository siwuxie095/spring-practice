package com.siwuxie095.spring.chapter15th.example6th.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.jaxws.SimpleJaxWsServiceExporter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * @author Jiajing Li
 * @date 2021-03-14 21:47:06
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter15th.example6th.*"})
public class JaxWsConfig {

    @Bean
    public SimpleJaxWsServiceExporter jaxwsExporter() {
        SimpleJaxWsServiceExporter exporter = new SimpleJaxWsServiceExporter();
        exporter.setBaseAddress("http://localhost:8889/services/");
        return exporter;
    }

    @Bean
    public HandlerMapping javawsMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        Properties mappings = new Properties();
        mappings.setProperty("/spitter.service", "javawsExportSpitterService");
        mapping.setMappings(mappings);
        return mapping;
    }

}

