package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.EmailRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.EqualRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.PasswordRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.UniqueEmailRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountsEntryFormValidator extends FormValidator {
    @Autowired
    public AccountsEntryFormValidator(AccountDao accountDao) {
        super(AccountsEntryForm.class,
                new NotEmptyRule("email"),
                new EmailRule("email"),
                new UniqueEmailRule(accountDao, Optional.empty(), "email"),
                new NotEmptyRule("password"),
                new PasswordRule("password"),
                new EqualRule("password", "retypedPassword"));
    }
}
