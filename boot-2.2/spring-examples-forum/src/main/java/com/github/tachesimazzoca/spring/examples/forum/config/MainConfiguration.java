package com.github.tachesimazzoca.spring.examples.forum.config;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import com.github.tachesimazzoca.spring.examples.forum.infrastructure.messaging.amqp.EmbeddedQpidBroker;

@Configuration
@ImportResource({"classpath:context-infrastructure-messaging-amqp.xml"})
//@ImportResource({"classpath:context-infrastructure-messaging-jms.xml"})
public class MainConfiguration {

    @Autowired
    private EmbeddedQpidBroker embeddedQpidBroker;

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean(destroyMethod = "shutdown")
    public EmbeddedQpidBroker embeddedQpidBroker() {
        return new EmbeddedQpidBroker();
    }

    @PostConstruct
    public void startupEmbeddedQpidBroker() throws Exception {
        embeddedQpidBroker.startup();
    }
}
