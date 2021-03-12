package com.siwuxie095.spring.chapter15th.example4th;

import com.siwuxie095.spring.chapter15th.example4th.service.SpitterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;

/**
 * @author Jiajing Li
 * @date 2021-03-10 22:29:04
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter15th.example4th.*"})
public class HessianClientConfig {

    @Bean
    public HessianProxyFactoryBean spitterService() {
        HessianProxyFactoryBean proxy = new HessianProxyFactoryBean();
        proxy.setServiceUrl("http://localhost:8080/Spitter/spitter.service");
        proxy.setServiceInterface(SpitterService.class);
        return proxy;
    }

}
