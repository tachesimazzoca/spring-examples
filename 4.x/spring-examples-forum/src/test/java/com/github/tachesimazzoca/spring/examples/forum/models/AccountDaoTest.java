package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.Assert.*;

public class AccountDaoTest {
    private static ApplicationContext context = new ClassPathXmlApplicationContext(
            "spring/database.xml");

    private DataSource dataSource() {
        return context.getBean("dataSource", DataSource.class);
    }

    private void resetTables(DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("TRUNCATE TABLE accounts");
        jdbcTemplate.execute("ALTER TABLE accounts ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    public void testSave() {
        DataSource ds = dataSource();
        resetTables(ds);

        AccountDao dao = new AccountDao(ds);
        Account account = new Account();
        account.setEmail("user1@example.net");
        account.setPasswordSalt("abcd");
        account.setPasswordHash("");
        account.setNickname("user1");
        account.setStatus(Account.Status.ACTIVE);

        account = dao.save(account);
        assertEquals(1L, account.getId().longValue());
        assertEquals("user1@example.net", account.getEmail());
        assertEquals("abcd", account.getPasswordSalt());
        assertEquals("", account.getPasswordHash());
        assertEquals("user1", account.getNickname());
        assertEquals(Account.Status.ACTIVE, account.getStatus());

        account.setPasswordSalt("bcde");
        account.setPasswordHash("hashedpw");
        account.setStatus(Account.Status.INACTIVE);
        account = dao.save(account);
        assertEquals(1L, account.getId().longValue());
        assertEquals("bcde", account.getPasswordSalt());
        assertEquals("hashedpw", account.getPasswordHash());
        assertEquals(Account.Status.INACTIVE, account.getStatus());
    }

    @Test
    public void testFindByEmail() {
        DataSource ds = dataSource();
        resetTables(ds);

        AccountDao dao = new AccountDao(ds);
        Account account = new Account();
        account.setEmail("user@example.net");
        account.setPasswordSalt("salt");
        account.setPasswordHash("pass");
        account.setNickname("user");
        account.setStatus(Account.Status.ACTIVE);
        dao.save(account);

        account = dao.findByEmail("user@example.net").get();
        assertEquals(1L, account.getId().longValue());
        assertEquals("user@example.net", account.getEmail());
        assertEquals("salt", account.getPasswordSalt());
        assertEquals("pass", account.getPasswordHash());
        assertEquals("user", account.getNickname());
    }
}
