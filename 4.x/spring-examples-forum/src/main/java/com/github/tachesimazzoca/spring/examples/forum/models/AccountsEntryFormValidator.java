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
    private final RegexValidator passwordRule = new RegexValidator("^[0-9a-zA-Z]{8,}$", false);

    @Override
    public boolean supports(Class<?> aClass) {
        return AccountsEntryForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        AccountsEntryForm form = (AccountsEntryForm) o;

        // email
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "NotEmpty.email");
        if (!errors.hasFieldErrors("email")) {
            if (!emailRule.isValid(form.getEmail())) {
                errors.rejectValue("email", "Pattern.email");
            }
        }
        if (!errors.hasFieldErrors("email")) {
            if (accountDao.findByEmail(form.getEmail()).isPresent()) {
                errors.rejectValue("email", "Unique.email");
            }
        }

        // password
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "password", "NotEmpty.password");
        if (!errors.hasFieldErrors("password")) {
            if (!passwordRule.isValid(form.getPassword())) {
                errors.rejectValue("email", "Pattern.password");
            }
        }
        if (!form.isValidRetypedPassword()) {
            errors.rejectValue("retypedPassword", "EqualTo.retypedPassword");
        }
    }
}
