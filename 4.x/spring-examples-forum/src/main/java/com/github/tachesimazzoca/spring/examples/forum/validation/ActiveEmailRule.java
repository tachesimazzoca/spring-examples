package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import org.springframework.validation.Errors;

import java.util.Optional;

public class ActiveEmailRule implements Rule {
    private static final String ERROR_CODE = ActiveEmailRule.class.getSimpleName();

    private final AccountDao accountDao;
    private final String field;
    private final String defaultMessage;

    public ActiveEmailRule(
            AccountDao accountDao,
            String field) {
        this(accountDao, field, ERROR_CODE);
    }

    public ActiveEmailRule(
            AccountDao accountDao,
            String field,
            String defaultMessage) {
        this.accountDao = accountDao;
        this.field = field;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        final String email = (String) errors.getFieldValue(field);
        final Optional<Account> accountOpt = accountDao.findByEmail(email);
        if (!accountOpt.isPresent() || !accountOpt.get().isActive())
            errors.rejectValue(field, ERROR_CODE, defaultMessage);
    }
}
