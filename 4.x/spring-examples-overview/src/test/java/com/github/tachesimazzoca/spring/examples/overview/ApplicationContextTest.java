package com.github.tachesimazzoca.spring.examples.overview;

import com.github.tachesimazzoca.spring.examples.overview.models.Account;
import com.github.tachesimazzoca.spring.examples.overview.models.AccountService;
import com.github.tachesimazzoca.spring.examples.overview.models.Config;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class ApplicationContextTest {
    @Test
    public void testXmlApplicationContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        // config
        Config config = context.getBean("config", Config.class);
        assertEquals("http://www.example.net", config.get("url.home"));

        // accountService
        AccountService accountService = context.getBean("accountService", AccountService.class);
        long id = 1234L;
        Account account = accountService.getAccountById(id);
        assertEquals(id, account.id.longValue());
    }
}
