package com.github.tachesimazzoca.spring.examples.forum.models;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AccountsLoginFormValidator implements Validator {
    private final EmailValidator emailRule = EmailValidator.getInstance(false);

    @Override
    public boolean supports(Class<?> aClass) {
        return AccountsLoginForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountsLoginForm form = (AccountsLoginForm) o;

        // email
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "notEmpty.email");
        if (!errors.hasFieldErrors("email")) {
            if (!emailRule.isValid(form.getEmail())) {
                errors.rejectValue("email", "pattern.email");
            }
        }

        // password
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "password", "notEmpty.password");
    }
}
