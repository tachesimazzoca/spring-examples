package com.github.tachesimazzoca.spring.examples.forum.application.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import com.github.tachesimazzoca.spring.examples.forum.application.AccountService;
import com.github.tachesimazzoca.spring.examples.forum.application.ApplicationEvents;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.Account;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.AccountRepository;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.Session;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.SessionRepository;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.account.AccountEntryAttempt;

public class AccountServiceImplTest {

    private static class MockAccountRepository implements AccountRepository {

        private Long accountSeq = 0L;

        private Map<Long, Account> table = new HashMap<>();

        @Override
        public Optional<Account> findByEmail(String email) {
            return Optional.empty();
        }

        @Override
        public <S extends Account> S save(S entity) {
            if (entity.getAccountId() == null) {
                accountSeq++;
                entity.setAccountId(Long.valueOf(accountSeq));
            }
            table.put(entity.getAccountId(), entity);
            return entity;
        }
    }

    private static class MockSessionRepository implements SessionRepository {

        private Map<String, Session> storage = new HashMap<>();

        @Override
        public void create(Session session) {
            update(session);
        }

        @Override
        public Optional<Session> read(String sessionId) {
            return Optional.of(storage.get(sessionId));
        }

        @Override
        public void update(Session session) {
            storage.put(session.getSessionId(), session);
        }

        @Override
        public void delete(String sessionId) {
            storage.remove(sessionId);
        }

        @Override
        public void gc(long lifetime) {
            storage.clear();
        }
    }

    private static class MockApplicationEvents implements ApplicationEvents {

        private final List<AccountEntryAttempt> queue;

        public MockApplicationEvents(List<AccountEntryAttempt> queue) {
            this.queue = queue;
        }

        @Override
        public void receivedAccountEntryAttempt(AccountEntryAttempt msg) {
            queue.add(msg);
        }
    }

    @Test
    public void testEntryAndActivate() {

        AccountRepository accountRepository = new MockAccountRepository();
        SessionRepository sessionRepository = new MockSessionRepository();
        List<AccountEntryAttempt> queue = new ArrayList<>();
        ApplicationEvents applicationEvents = new MockApplicationEvents(queue);
        AccountService accountService = new AccountServiceImpl(
                accountRepository, sessionRepository, applicationEvents);

        Session s = accountService.attempt("foo@example.net", "changeme");
        Account a = accountService.activate(s.getSessionId()).get();

        assertEquals("foo@example.net", a.getEmail());
        assertTrue(a.isValidPassword("changeme"));
        assertTrue(a.isActive());
        assertEquals(1, queue.size());
        assertEquals(s.getSessionId() , queue.get(0).getSessionId());
    }
}
