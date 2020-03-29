package com.github.tachesimazzoca.spring.examples.forum.application.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.tachesimazzoca.spring.examples.forum.application.AccountService;
import com.github.tachesimazzoca.spring.examples.forum.application.ApplicationEvents;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.Account;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.AccountRepository;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.AccountStatus;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.Session;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.SessionRepository;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.account.AccountEntryAttempt;

@Component
public class AccountServiceImpl implements AccountService {

    private final String KEY_EMAIL = "email";
    private final String KEY_PASSWORD = "password";

    private final AccountRepository accountRepository;

    private final SessionRepository sessionRepository;

    private final ApplicationEvents applicationEvents;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
            SessionRepository sessionRepository,
            ApplicationEvents applicationEvents) {
        this.accountRepository = accountRepository;
        this.sessionRepository = sessionRepository;
        this.applicationEvents = applicationEvents;
    }

    @Override
    public Session attempt(String email, String password) {
        Session s = Session.create();
        s.getValueMap().put(KEY_EMAIL, email);
        s.getValueMap().put(KEY_PASSWORD, password);
        sessionRepository.create(s);

        AccountEntryAttempt msg = new AccountEntryAttempt();
        msg.setSessionId(s.getSessionId());
        msg.setEmail(email);
        applicationEvents.receivedAccountEntryAttempt(msg);

        return s;
    }

    @Override
    public Optional<Account> activate(String sessionId) {
        Optional<Session> sOpt = sessionRepository.read(sessionId);
        if (!sOpt.isPresent()) {
            return Optional.empty();
        }

        Session s = sOpt.get();
        // TODO: Validate the value map ...
        Account account = new Account();
        account.setEmail(s.getValueMap().get(KEY_EMAIL));
        account.refreshPassword(s.getValueMap().get(KEY_PASSWORD));
        account.setAccountStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
        return Optional.of(account);
    }
}
