package com.github.tachesimazzoca.spring.examples.forum.models;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AccountsEntryFormValidator implements Validator {
    @Autowired
    private AccountDao accountDao;

    private final EmailValidator emailRule = EmailValidator.getInstance(false);
    private final RegexValidator passwordRule = new RegexValidator("^.{4,}$", false);

    @Override
    public boolean supports(Class<?> aClass) {
        return AccountsEntryForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountsEntryForm form = (AccountsEntryForm) o;

        checkEmail(form, errors);
        checkPassword(form, errors);
    }

    private void checkEmail(AccountsEntryForm form, Errors errors) {
        // Check if the e-mail address is not empty.
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "NotEmpty.email");
        if (errors.hasFieldErrors("email"))
            return;

        // Check if the e-mail address is valid.
        if (!emailRule.isValid(form.getEmail())) {
            errors.rejectValue("email", "Pattern.email");
            return;
        }

        // Check if the e-mail address hasn't been registered by another user.
        if (accountDao.findByEmail(form.getEmail()).isPresent()) {
            errors.rejectValue("email", "Unique.email");
        }
    }

    private void checkPassword(AccountsEntryForm form, Errors errors) {
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
