package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyChecker;
import org.springframework.stereotype.Component;

@Component
public class AccountLoginFormValidator extends FormValidator {
    public AccountLoginFormValidator() {
        setAssignableClass(AccountLoginForm.class);
        addRule("email", new NotEmptyChecker());
        addRule("password", new NotEmptyChecker());
    }
}
