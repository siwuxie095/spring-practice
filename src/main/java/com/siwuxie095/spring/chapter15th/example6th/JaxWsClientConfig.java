package com.siwuxie095.spring.chapter15th.example6th;

import com.siwuxie095.spring.chapter15th.example6th.service.SpitterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.jaxws.JaxWsPortProxyFactoryBean;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Jiajing Li
 * @date 2021-03-14 21:56:00
 */
@SuppressWarnings("all")
@Configuration
@ComponentScan(basePackages = {"com.siwuxie095.spring.chapter15th.example6th.*"})
public class JaxWsClientConfig {

    @Bean
    public JaxWsPortProxyFactoryBean spitterService() throws MalformedURLException {
        JaxWsPortProxyFactoryBean proxy = new JaxWsPortProxyFactoryBean();
        proxy.setWsdlDocumentUrl(new URL("http://localhost:8889/services/SpitterService?wsdl"));
        proxy.setServiceName("spitterService");
        proxy.setPortName("spitterServiceHttpPort");
        proxy.setServiceInterface(SpitterService.class);
        proxy.setNamespaceUri("http://spitter.com");
        return proxy;
    }

}

