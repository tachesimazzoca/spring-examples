package com.github.tachesimazzoca.spring.examples.forum.application;

import com.github.tachesimazzoca.spring.examples.forum.interfaces.account.AccountEntryAttempt;

public interface ApplicationEvents {
    void receivedAccountEntryAttempt(AccountEntryAttempt msg);
}
