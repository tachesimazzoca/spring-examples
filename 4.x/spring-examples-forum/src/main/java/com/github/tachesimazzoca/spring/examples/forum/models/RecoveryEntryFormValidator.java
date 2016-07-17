package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.ActiveEmailRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecoveryEntryFormValidator extends FormValidator {
    @Autowired
    public RecoveryEntryFormValidator(AccountDao accountDao) {
        super(RecoveryEntryForm.class,
                new NotEmptyRule("email"),
                new ActiveEmailRule(accountDao, "email"));
    }
}
