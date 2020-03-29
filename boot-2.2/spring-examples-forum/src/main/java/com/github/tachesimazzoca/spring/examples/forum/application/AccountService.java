package com.github.tachesimazzoca.spring.examples.forum.application;

import java.util.Optional;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.Account;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.Session;

public interface AccountService {

    Session attempt(String email, String password);

    Optional<Account> activate(String sessionId);
}
