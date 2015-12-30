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

        checkEmail(form, errors);
        checkPassword(form, errors);
    }

    private void checkEmail(AccountsLoginForm form, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "NotEmpty.email");
        if (errors.hasFieldErrors("email"))
            return;

        if (!emailRule.isValid(form.getEmail())) {
            errors.rejectValue("email", "Pattern.email");
        }
    }

    private void checkPassword(AccountsLoginForm form, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "password", "NotEmpty.password");
    }
}
