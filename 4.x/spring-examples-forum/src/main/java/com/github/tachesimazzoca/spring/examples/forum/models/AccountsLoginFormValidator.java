package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyRule;
import org.springframework.stereotype.Component;

@Component
public class AccountsLoginFormValidator extends FormValidator {
    public AccountsLoginFormValidator() {
        super(AccountsLoginForm.class,
                new NotEmptyRule("email"),
                new NotEmptyRule("password"));
    }
}
