package com.github.tachesimazzoca.spring.examples.tx.service;

import com.github.tachesimazzoca.spring.examples.tx.entity.Account;
import org.h2.jdbc.JdbcSQLException;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/test/spring/application.xml")
public class AccountServiceTest {
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("subDataSource")
    private DataSource subDataSource;

    @Autowired
    private AccountService accountService;

    private void resetTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("DROP TABLE IF EXISTS accounts");
        jdbcTemplate.execute("CREATE TABLE accounts" +
                " (id BIGINT PRIMARY KEY, username VARCHAR(255), modified_at DATETIME)");

        JdbcTemplate subJdbcTemplate = new JdbcTemplate(subDataSource);
        subJdbcTemplate.execute("DROP TABLE IF EXISTS account_log");
        subJdbcTemplate.execute("CREATE TABLE account_log" +
                " (id BIGINT AUTO_INCREMENT PRIMARY KEY, access_date DATETIME, message VARCHAR(255))");
    }

    @Test
    public void testGetAccount() {
        resetTables();
        Account[] accounts = new Account[1];
        accounts[0] = new Account(1L, "user1", null);
        Account[] savedAccounts = accountService.createAccounts(accounts);
        for (int i = 0; i < 100; i++) {
            assertEquals(savedAccounts[0], accountService.getAccount(1L));
        }
    }

    @Test
    public void testCreateAccounts() {
        resetTables();
        final int N = 100;
        Account[] accounts = new Account[N];
        for (int i = 0; i < N; i++) {
            long id = i + 1;
            accounts[i] = new Account(id, "user" + id, null);
        }
        Account[] savedAccounts = accountService.createAccounts(accounts);
        assertEquals(N, savedAccounts.length);

        for (Account account : savedAccounts) {
            accountService.writeAccountLog("saved " + account.toString());
        }
        JdbcTemplate subJdbcTemplate = new JdbcTemplate(subDataSource);
        SqlRowSet rowSet = subJdbcTemplate.queryForRowSet(
                "SELECT * FROM account_log ORDER BY id");
        while (rowSet.next()) {
            System.out.println(String.format("%d %s %s",
                    rowSet.getLong("id"),
                    rowSet.getString("access_date"),
                    rowSet.getString("message")));
        }
    }

    @Test
    public void testCreateAccountsWithRollback() {
        resetTables();
        Account[] accounts = new Account[4];
        accounts[0] = new Account(1L, "user1", null);
        accounts[1] = new Account(2L, "user2", null);
        accounts[2] = new Account(3L, "user3", null);
        accounts[3] = new Account(1L, "user4", null); // primary key violation
        try {
            Account[] savedAccounts = accountService.createAccounts(accounts);
            fail("It should throw JdbcSQLIntegrityConstraintViolationException");
        } catch (Exception e) {
            assertEquals(JdbcSQLIntegrityConstraintViolationException.class, e.getCause().getClass());
            System.out.println(e.getMessage());
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM accounts", Long.class);
        assertEquals(0L, count.longValue());
    }
}
