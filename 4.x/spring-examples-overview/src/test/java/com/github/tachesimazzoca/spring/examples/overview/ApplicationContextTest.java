package com.github.tachesimazzoca.spring.examples.overview;

import com.github.tachesimazzoca.spring.examples.overview.models.Account;
import com.github.tachesimazzoca.spring.examples.overview.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.overview.models.AccountService;
import com.github.tachesimazzoca.spring.examples.overview.config.Config;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class ApplicationContextTest {
    private Properties createPropertiesByClassPath(String path) throws IOException {
        Properties props = new Properties();
        props.load(this.getClass().getResourceAsStream(path));
        return props;
    }

    private static <T> Set<T> set(T... values) {
        return new LinkedHashSet<T>(Arrays.asList(values));
    }

    @Test
    public void testXmlApplicationContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/application.xml");

        // config
        Config config = context.getBean("config", Config.class);
        assertEquals("http://www.example.net", config.get("url.home"));
        assertEquals("/spring-examples-overview", config.get("url.base"));

        // accountDao
        AccountDao dao1 = context.getBean("mockAccountDao", AccountDao.class);
        AccountDao dao2 = context.getBean("accountDao", AccountDao.class);
        assertTrue(dao1 == dao2);

        // accountService
        AccountService accountService = context.getBean("accountService", AccountService.class);
        long id = 1234L;
        Account account = accountService.getAccountById(id);
        assertEquals(id, account.id.longValue());
    }

    @Test
    public void testUtilSchema() throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/util.xml");

        // util:constant - FieldRetrievingFactoryBean
        Locale locale = context.getBean("locale", Locale.class);
        assertEquals(Locale.US, locale);

        // util:property-path - PropertyPathFactoryBean
        Long adminId = context.getBean("adminId", Long.class);
        assertEquals(1L, adminId.longValue());
        Long guestId = context.getBean("guestId", Long.class);
        assertEquals(2L, guestId.longValue());

        // util:properties - PropertiesFactoryBean
        Properties configA = context.getBean("configA", Properties.class);
        assertEquals(createPropertiesByClassPath("/config.properties"), configA);
        Properties configB = context.getBean("configB", Properties.class);
        assertEquals(configA, configB);

        // util:list - ListFactoryBean
        List<String> emailList = (List<String>) context.getBean("emailList", List.class);
        assertEquals(2, emailList.size());
        assertEquals("user1@example.net", emailList.get(0));
        assertEquals("user2@example.net", emailList.get(1));

        // util:map - MapFactoryBean
        Map<Long, String> emailMap = (Map<Long, String>) context.getBean("emailMap", Map.class);
        assertEquals("LinkedHashMap", emailMap.getClass().getSimpleName());
        assertEquals(2, emailMap.size());
        assertEquals("user1@example.net", emailMap.get(1L));
        assertEquals("user2@example.net", emailMap.get(2L));

        // util:set - SetFactoryBean
        Set<Double> numberSet = (Set<Double>) context.getBean("numberSet", Set.class);
        assertEquals("LinkedHashSet", numberSet.getClass().getSimpleName());
        assertEquals(3, numberSet.size());
        assertEquals(set(12.3, 45.6, 78.9), numberSet);
    }
}
