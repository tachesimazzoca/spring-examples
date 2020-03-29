package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.github.tachesimazzoca.spring.examples.forum.config.TestApplicationConfig;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.Account;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.AccountStatus;

@ExtendWith(SpringExtension.class)
@Import(value = {TestApplicationConfig.class})
@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class JpaAccountRepositoryTest {

    @Autowired
    private JpaAccountRepository accountRepository;

    @Test
    public void testSaveAndFind() {
        Account[] accounts = new Account[3];
        accounts[0] = createAccount(null, "1");
        accounts[1] = createAccount(null, "2");
        accounts[2] = createAccount(null, "3");
        for (Account x : accounts) {
            accountRepository.save(x);
        }
        for (Account x : accounts) {
            Optional<Account> entity = accountRepository.findByEmail(x.getEmail());
            assertTrue(entity.isPresent());
            assertEquals(x, entity.get());
        }
    }

    @Test
    public void testEmptyResult() {
        assertFalse(accountRepository.findByEmail("nope").isPresent());
    }

    private static Account createAccount(Long accountId, String tag) {
        Account entity = new Account();
        entity.setAccountId(accountId);
        entity.setEmail(String.format("user-%s@example.net", tag));
        entity.setNickname(String.format("user-%s", tag));
        entity.setAccountStatus(AccountStatus.ACTIVE);
        entity.refreshPassword(String.format("password-%s", tag));
        return entity;
    }
}
