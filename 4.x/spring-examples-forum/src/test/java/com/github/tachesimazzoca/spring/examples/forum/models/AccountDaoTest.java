package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

import static org.junit.Assert.*;

public class AccountDaoTest {
    private static ApplicationContext context = new ClassPathXmlApplicationContext(
            "spring/database.xml");

    private DataSource dataSource() {
        return context.getBean("dataSource", DataSource.class);
    }

    @Test
    public void testCRUD() {
        AccountDao dao = new AccountDao(dataSource());
        Account account = dao.create(
                new Account(null, "user1@example.net", "", "", "user1", Account.Status.ACTIVE));
        assertEquals(1L, account.id.longValue());
        assertEquals("user1@example.net", account.email);
        assertEquals("user1", account.nickname);
        assertEquals(Account.Status.ACTIVE, account.status);

        Account updatedAccount = dao.update(new Account(account.id, account.email,
                account.passwordSalt, account.passwordHash, "user1-1", account.status.INACTIVE));
        assertEquals(1L, updatedAccount.id.longValue());
        assertEquals("user1@example.net", updatedAccount.email);
        assertEquals("user1-1", updatedAccount.nickname);
        assertEquals(Account.Status.INACTIVE, updatedAccount.status);
    }
}
