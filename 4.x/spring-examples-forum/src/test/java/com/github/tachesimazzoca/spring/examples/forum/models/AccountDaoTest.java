package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class AccountDaoTest {
    @Autowired
    private DataSource dataSource;

    private void resetTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE accounts");
        jdbcTemplate.execute("ALTER TABLE accounts ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    public void testSave() {
        resetTables();

        AccountDao dao = new AccountDao(dataSource);
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
        resetTables();

        AccountDao dao = new AccountDao(dataSource);
        Account account = new Account();
        account.setEmail("user@example.net");
        account.setPasswordSalt("salt");
        account.setPasswordHash("pass");
        account.setNickname("user");
        account.setStatus(Account.Status.ACTIVE);
        dao.save(account);

        account = dao.findByEmail("user@example.net").get();
        assertNotNull(account.getId());
        assertEquals("user@example.net", account.getEmail());
        assertEquals("salt", account.getPasswordSalt());
        assertEquals("pass", account.getPasswordHash());
        assertEquals("user", account.getNickname());
    }
}
