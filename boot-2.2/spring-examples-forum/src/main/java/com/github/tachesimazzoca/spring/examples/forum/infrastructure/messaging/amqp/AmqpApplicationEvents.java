package com.github.tachesimazzoca.spring.examples.forum.infrastructure.messaging.amqp;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.tachesimazzoca.spring.examples.forum.application.ApplicationEvents;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.account.AccountEntryAttempt;

@Component
public class AmqpApplicationEvents implements ApplicationEvents {

    private final AmqpAdmin amqpAdmin;

    private final AmqpTemplate amqpTemplate;

    private final Queue accountEntryAttemptQueue = new Queue("accountEntryAttempt");

    @Autowired
    public AmqpApplicationEvents(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate) {
        this.amqpAdmin = amqpAdmin;
        this.amqpTemplate = amqpTemplate;

        this.amqpAdmin.declareQueue(accountEntryAttemptQueue);
    } 

    public void receivedAccountEntryAttempt(AccountEntryAttempt msg) {
        amqpTemplate.convertAndSend(accountEntryAttemptQueue.getName(), msg);
    }
}
