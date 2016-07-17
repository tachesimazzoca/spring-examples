package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.EqualRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.PasswordRule;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RecoveryResetFormValidator extends FormValidator {
    public RecoveryResetFormValidator() {
        super(RecoveryResetForm.class,
                new NotEmptyRule("password"),
                new PasswordRule("password"),
                new EqualRule("password", "retypedPassword"));
    }
}
