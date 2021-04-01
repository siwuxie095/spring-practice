package com.siwuxie095.spring.chapter20th.example3rd.cfg;

import com.siwuxie095.spring.chapter20th.example3rd.web.SpittleControllerManagedOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.access.MBeanProxyFactoryBean;
import org.springframework.jmx.support.ConnectorServerFactoryBean;
import org.springframework.jmx.support.MBeanServerConnectionFactoryBean;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import java.net.MalformedURLException;

/**
 * @author Jiajing Li
 * @date 2021-04-01 08:07:30
 */
@SuppressWarnings("all")
@Configuration
public class Config {

    @Bean
    public ConnectorServerFactoryBean connectorServerFactoryBean() {
        return new ConnectorServerFactoryBean();
    }

    @Bean
    public ConnectorServerFactoryBean rmiConnectorServerFactoryBean() {
        ConnectorServerFactoryBean csfb = new ConnectorServerFactoryBean();
        csfb.setServiceUrl(
                "service:jmx:rmi://localhost/jndi/rmi://localhost:1099/spitter");
        return csfb;
    }

    @Bean
    public RmiRegistryFactoryBean rmiRegistryFB() {
        RmiRegistryFactoryBean rmiRegistryFB = new RmiRegistryFactoryBean();
        rmiRegistryFB.setPort(1099);
        return rmiRegistryFB;
    }

    @Bean
    public MBeanServerConnectionFactoryBean connectionFactoryBean()
            throws MalformedURLException {
        MBeanServerConnectionFactoryBean mbscfb =
                new MBeanServerConnectionFactoryBean();
        mbscfb.setServiceUrl(
                "service:jmx:rmi://localhost/jndi/rmi://localhost:1099/spitter");
        return mbscfb;
    }

    @Bean
    public JmxClient jmxClient(MBeanServerConnection connection) {
        JmxClient jmxClient = new JmxClient();
        jmxClient.setMBeanServerConnection(connection);
        return jmxClient;
    }

    @Bean
    public MBeanProxyFactoryBean remoteSpittleControllerMBean(
            MBeanServerConnection mbeanServerClient)
            throws MalformedObjectNameException {
        MBeanProxyFactoryBean proxy = new MBeanProxyFactoryBean();
        proxy.setObjectName("");
        proxy.setServer(mbeanServerClient);
        proxy.setProxyInterface(SpittleControllerManagedOperations.class);
        return proxy;
    }

}
