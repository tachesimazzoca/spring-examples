package com.github.tachesimazzoca.spring.examples.forum.infrastructure.messaging.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.account.AccountEntryAttempt;

public class JmsAccountEntryAttemptConsumer implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(JmsAccountEntryAttemptConsumer.class);

    @Override
    public void onMessage(Message message) {
        try {
            ObjectMessage om = (ObjectMessage) message;
            AccountEntryAttempt attempt = (AccountEntryAttempt) om.getObject();
            logger.info("Received accountEntryAttempt: " + attempt);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
