package com.siwuxie095.spring.chapter15th.example4th.cfg;

import com.siwuxie095.spring.chapter15th.example4th.service.SpitterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.BurlapServiceExporter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * @author Jiajing Li
 * @date 2021-03-10 22:26:24
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter15th.example4th.*"})
public class BurlapConfig {

    @Bean
    public BurlapServiceExporter burlapExporterSpitterService(SpitterService service) {
        BurlapServiceExporter exporter = new BurlapServiceExporter();
        exporter.setService(service);
        exporter.setServiceInterface(SpitterService.class);
        return exporter;
    }

    @Bean
    public HandlerMapping burlapMapping() {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        Properties mappings = new Properties();
        mappings.setProperty("/spitter.service", "burlapExportSpitterService");
        handlerMapping.setMappings(mappings);
        return handlerMapping;
    }

}

