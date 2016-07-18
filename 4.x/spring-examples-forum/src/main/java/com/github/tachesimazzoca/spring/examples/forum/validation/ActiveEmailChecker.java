package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import org.springframework.validation.Errors;

import java.util.Optional;

public class ActiveEmailChecker implements Checker {
    private final AccountDao accountDao;

    public ActiveEmailChecker(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        final Optional<Account> accountOpt = accountDao.findByEmail((String) fieldValue);
        return (accountOpt.isPresent() && accountOpt.get().isActive());
    }
}
