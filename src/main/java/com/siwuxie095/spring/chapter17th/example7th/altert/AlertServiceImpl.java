package com.siwuxie095.spring.chapter17th.example7th.altert;

import com.siwuxie095.spring.chapter17th.example7th.domain.Spittle;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * @author Jiajing Li
 * @date 2021-03-22 21:48:50
 */
@SuppressWarnings("all")
public class AlertServiceImpl implements AlertService {

    private JmsOperations jmsOperations;

    public AlertServiceImpl(JmsOperations jmsOperations) {
        this.jmsOperations = jmsOperations;
    }

    //public void sendSpittleAlert(final Spittle spittle) {
    //    jmsOperations.send(
    //            "spittle.alert.queue",
    //            new MessageCreator() {
    //                public Message createMessage(Session session)
    //                        throws JMSException {
    //                    return session.createObjectMessage(spittle);
    //                }
    //            });
    //}


    //public void sendSpittleAlert(final Spittle spittle) {
    //    jmsOperations.send(
    //            new MessageCreator() {
    //                public Message createMessage(Session session)
    //                        throws JMSException {
    //                    return session.createObjectMessage(spittle);
    //                }
    //            });
    //  }


    @Override
    public void sendSpittleAlert(Spittle spittle) {
        jmsOperations.convertAndSend(spittle);
    }

    //public Spittle getSpittleAlert() {
    //    try {
    //        ObjectMessage message = (ObjectMessage) jmsOperations.receive();
    //        return (Spittle) message.getObject();
    //    } catch (JMSException e) {
    //        throw JmsUtils.convertJmsAccessException(e);
    //    }
    //}

    @Override
    public Spittle retrieveSpittleAlert() {
        return (Spittle) jmsOperations.receiveAndConvert();
    }

}

