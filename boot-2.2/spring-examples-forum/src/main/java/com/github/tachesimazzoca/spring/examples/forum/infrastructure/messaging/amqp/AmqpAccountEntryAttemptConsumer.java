package com.github.tachesimazzoca.spring.examples.forum.infrastructure.messaging.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.account.AccountEntryAttempt;

@Component
public class AmqpAccountEntryAttemptConsumer {

    private final Logger logger = LoggerFactory.getLogger(AmqpAccountEntryAttemptConsumer.class);

    @RabbitListener(queues = "accountEntryAttempt")
    public void listen(AccountEntryAttempt attempt) {
        try {
            logger.info("Received accountEntryAttempt: " + attempt);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
