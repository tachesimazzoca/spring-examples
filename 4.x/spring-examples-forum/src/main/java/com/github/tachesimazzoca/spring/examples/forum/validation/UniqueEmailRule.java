package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;

import java.util.Optional;

public class UniqueEmailRule implements Rule {
    private final AccountDao accountDao;
    private final Optional<String> accountPropertyPath;
    private final String field;
    private final String message;

    public UniqueEmailRule(
            AccountDao accountDao,
            Optional<String> accountPropertyPath,
            String field) {
        this(accountDao, accountPropertyPath, field, UniqueEmailRule.class.getSimpleName());
    }

    public UniqueEmailRule(
            AccountDao accountDao,
            Optional<String> accountPropertyPath,
            String field,
            String message) {
        this.accountDao = accountDao;
        this.accountPropertyPath = accountPropertyPath;
        this.field = field;
        this.message = message;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        final String email = (String) errors.getFieldValue(field);
        final BeanWrapper beanWrapper = new BeanWrapperImpl(o);
        if (accountPropertyPath.isPresent()) {
            final Account currentAccount = (Account) beanWrapper.getPropertyValue(accountPropertyPath.get());
            if (currentAccount.getEmail().equals(email))
                return;
        }
        final Optional<Account> accountOpt = accountDao.findByEmail(email);
        if (accountOpt.isPresent())
            errors.rejectValue(field, message);
    }
}
