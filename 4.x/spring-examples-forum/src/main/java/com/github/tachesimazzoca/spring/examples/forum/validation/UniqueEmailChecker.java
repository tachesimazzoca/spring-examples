package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;

import java.util.Optional;

public class UniqueEmailChecker implements Checker {
    private final AccountDao accountDao;
    private final Optional<String> accountPropertyPath;

    public UniqueEmailChecker(AccountDao accountDao, Optional<String> accountPropertyPath) {
        this.accountDao = accountDao;
        this.accountPropertyPath = accountPropertyPath;
    }

    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        final String email = (String) fieldValue;
        final BeanWrapper beanWrapper = new BeanWrapperImpl(o);
        if (accountPropertyPath.isPresent()) {
            final Account currentAccount = (Account) beanWrapper.getPropertyValue(accountPropertyPath.get());
            if (currentAccount.getEmail().equals(email))
                return true;
        }
        final Optional<Account> accountOpt = accountDao.findByEmail(email);
        return !accountOpt.isPresent();
    }
}
