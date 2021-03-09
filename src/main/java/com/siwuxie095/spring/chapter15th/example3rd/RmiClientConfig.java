package com.siwuxie095.spring.chapter15th.example3rd;

import com.siwuxie095.spring.chapter15th.example3rd.service.SpitterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

/**
 * @author Jiajing Li
 * @date 2021-03-09 22:44:44
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter15th.example3rd.*"})
public class RmiClientConfig {

    @Bean
    public RmiProxyFactoryBean spitterService() {
        RmiProxyFactoryBean proxyFactoryBean = new RmiProxyFactoryBean();
        proxyFactoryBean.setServiceUrl("rmi://localhost/SpitterService");
        proxyFactoryBean.setServiceInterface(SpitterService.class);
        return proxyFactoryBean;
    }

}

