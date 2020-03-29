package com.github.tachesimazzoca.spring.examples.forum.domain.model.account;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AccountTest {
    @Test
    public void testPassword() {
        Account account = new Account();
        account.refreshPassword("changeme");
        assertTrue(account.isValidPassword("changeme"));
    }

    @Test
    public void testPasswordWithSalt() {
        Account account = new Account();
        account.refreshPassword("changeme", "a1B2");
        assertTrue(account.isValidPassword("changeme"));
    }

    @Test
    public void testPasswordWithInvalidSalt() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account account = new Account();
            account.refreshPassword("changeme", "");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Account account = new Account();
            account.refreshPassword("changeme", "123");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            Account account = new Account();
            account.refreshPassword("changeme", "12345");
        });
    }
}
