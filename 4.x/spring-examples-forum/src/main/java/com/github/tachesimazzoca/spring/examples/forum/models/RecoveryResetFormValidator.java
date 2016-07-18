package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.EqualChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.PasswordChecker;
import org.springframework.stereotype.Component;

@Component
public class RecoveryResetFormValidator extends FormValidator {
    public RecoveryResetFormValidator() {
        setAssignableClass(RecoveryResetForm.class);
        addRule("password", new PasswordChecker());
        addRule("retypedPassword", new EqualChecker("password"));
    }
}
