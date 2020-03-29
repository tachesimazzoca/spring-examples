package com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation;

import java.util.Optional;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.Account;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.AccountRepository;

public class UniqueAccountEmailChecker implements Checker {

    private final AccountRepository accountRepository;
    private final Optional<String> accountPropertyPath;

    public UniqueAccountEmailChecker(AccountRepository accountRepository,
            Optional<String> accountPropertyPath) {
        this.accountRepository = accountRepository;
        this.accountPropertyPath = accountPropertyPath;
    }

    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        final String email = (String) fieldValue;
        final BeanWrapper beanWrapper = new BeanWrapperImpl(o);
        if (accountPropertyPath.isPresent()) {
            final Account currentAccount =
                    (Account) beanWrapper.getPropertyValue(accountPropertyPath.get());
            if (currentAccount.getEmail().equals(email))
                return true;
        }
        final Optional<Account> accountOpt = accountRepository.findByEmail(email);
        return !accountOpt.isPresent();
    }
}
