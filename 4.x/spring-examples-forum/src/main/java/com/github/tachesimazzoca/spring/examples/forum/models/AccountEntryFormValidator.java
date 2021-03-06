package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.EmailChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.EqualChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.PasswordChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.UniqueEmailChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountEntryFormValidator extends FormValidator {
    @Autowired
    public AccountEntryFormValidator(AccountDao accountDao) {
        setAssignableClass(AccountEntryForm.class);
        addRule("email", new EmailChecker());
        addRule("email", new UniqueEmailChecker(accountDao, Optional.<String>empty()));
        addRule("password", new PasswordChecker());
        addRule("retypedPassword", new EqualChecker("password"));
    }
}
