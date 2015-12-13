package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AccountsEntryFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return AccountsEntryForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "AccountsEntryForm.email.NotEmpty");
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "password", "AccountsEntryForm.password.NotEmpty");
        AccountsEntryForm form = (AccountsEntryForm) o;
        if (!form.isValidRetypedPassword()) {
            errors.rejectValue("retypedPassword", "AccountsEntryForm.retypedPassword.Match");
        }
        if (!form.isUniqueEmail()) {
            errors.rejectValue("email", "AccountsEntryForm.email.Unique");
        }
    }
}
