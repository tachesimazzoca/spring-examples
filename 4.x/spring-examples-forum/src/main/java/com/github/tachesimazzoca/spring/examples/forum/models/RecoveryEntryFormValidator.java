package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.ActiveEmailChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecoveryEntryFormValidator extends FormValidator {
    @Autowired
    public RecoveryEntryFormValidator(AccountDao accountDao) {
        setAssignableClass(RecoveryEntryForm.class);
        addRule("email", new NotEmptyChecker());
        addRule("email", new ActiveEmailChecker(accountDao));
    }
}
