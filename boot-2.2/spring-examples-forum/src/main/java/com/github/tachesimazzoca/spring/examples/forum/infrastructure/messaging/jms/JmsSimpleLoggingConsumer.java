package com.github.tachesimazzoca.spring.examples.forum.infrastructure.messaging.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmsSimpleLoggingConsumer implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(JmsSimpleLoggingConsumer.class);

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage om = (ObjectMessage) message;
            logger.info("Received JMS message: " + om.getObject().toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
