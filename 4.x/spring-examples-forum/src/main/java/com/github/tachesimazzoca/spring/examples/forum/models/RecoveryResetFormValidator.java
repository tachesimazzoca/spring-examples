package com.github.tachesimazzoca.spring.examples.forum.models;

import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RecoveryResetFormValidator implements Validator {
    private final RegexValidator passwordRule = new RegexValidator("^.{4,}$", false);

    @Override
    public boolean supports(Class<?> aClass) {
        return RecoveryResetForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RecoveryResetForm form = (RecoveryResetForm) o;

        checkPassword(form, errors);
    }

    private void checkPassword(RecoveryResetForm form, Errors errors) {
        // Check if the password is not empty.
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "password", "NotEmpty.password");
        if (errors.hasFieldErrors("password"))
            return;

        // Check if the password is valid.
        if (!passwordRule.isValid(form.getPassword())) {
            errors.rejectValue("password", "Pattern.password");
            return;
        }
        // Check if the password is equals to the re-typed one.
        if (!form.isValidRetypedPassword()) {
            errors.rejectValue("retypedPassword", "EqualTo.retypedPassword");
        }
    }
}
