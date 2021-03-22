package com.siwuxie095.spring.chapter17th.example7th;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

/**
 * @author Jiajing Li
 * @date 2021-03-22 07:51:26
 */
@SuppressWarnings("all")
public class OutOfControl {

    /**
     * 使用传统的 JMS（不使用 Spring）发送消息
     */
    public void sendMessage() {
        ConnectionFactory cf =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection conn = null;
        Session session = null;
        try {
            conn = cf.createConnection();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = new ActiveMQQueue("spitter.queue");
            MessageProducer producer = session.createProducer(destination);
            TextMessage message = session.createTextMessage();
            message.setText("Hello World!");
            producer.send(message);
        } catch (JMSException e) {
            // handle exception
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (JMSException e) {
                // ...
            }
        }
    }


    /**
     * 使用传统的 JMS（不使用 Spring）接收消息
     */
    public void recieveMessage() {
        ConnectionFactory cf =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection conn = null;
        Session session = null;
        try {
            conn = cf.createConnection();
            conn.start();
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = new ActiveMQQueue("spitter.queue");
            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive();
            TextMessage textMessage = (TextMessage) message;
            System.out.println("GOT A MESSAGE: " + textMessage.getText());
            conn.start();
        } catch (JMSException e) {
            // handle exception
        } finally {
            try {
                if (session != null) {
                    session.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (JMSException e) {
                // ...
            }
        }
    }

}
