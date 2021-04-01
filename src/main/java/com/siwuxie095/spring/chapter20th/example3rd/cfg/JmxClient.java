package com.siwuxie095.spring.chapter20th.example3rd.cfg;

import javax.management.MBeanServerConnection;

/**
 * @author Jiajing Li
 * @date 2021-04-01 20:55:51
 */
@SuppressWarnings("all")
public class JmxClient {

    private MBeanServerConnection mbeanServerConnection;

    public MBeanServerConnection getMBeanServerConnection() {
        return mbeanServerConnection;
    }

    public void setMBeanServerConnection(MBeanServerConnection mbeanServerConnection) {
        this.mbeanServerConnection = mbeanServerConnection;
    }
}
