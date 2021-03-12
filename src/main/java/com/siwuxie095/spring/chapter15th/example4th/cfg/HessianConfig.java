package com.siwuxie095.spring.chapter15th.example4th.cfg;

import com.siwuxie095.spring.chapter15th.example4th.service.SpitterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * @author Jiajing Li
 * @date 2021-03-10 22:27:12
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter15th.example4th.*"})
public class HessianConfig {

    @Bean
    public HessianServiceExporter hessianExporterSpitterService(SpitterService spitterService) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(spitterService);
        exporter.setServiceInterface(SpitterService.class);
        return exporter;
    }

    @Bean
    public HandlerMapping hessianMapping() {
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        Properties mappings = new Properties();
        mappings.setProperty("/spitter.service", "hessianExportSpitterService");
        handlerMapping.setMappings(mappings);
        return handlerMapping;
    }

}
