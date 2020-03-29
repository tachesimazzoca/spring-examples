package com.github.tachesimazzoca.spring.examples.forum.infrastructure.messaging.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import com.github.tachesimazzoca.spring.examples.forum.application.ApplicationEvents;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.account.AccountEntryAttempt;

public class JmsApplicationEvents implements ApplicationEvents {

    private Destination accountEntryAttemptQueue;

    @Autowired
    private JmsTemplate jmsQueueOperations;

    public void receivedAccountEntryAttempt(AccountEntryAttempt msg) {
        jmsQueueOperations.send(accountEntryAttemptQueue, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(msg);
            }
        });
    }
}
