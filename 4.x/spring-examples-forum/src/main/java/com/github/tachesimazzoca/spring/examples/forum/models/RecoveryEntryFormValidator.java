package com.github.tachesimazzoca.spring.examples.forum.models;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RecoveryEntryFormValidator implements Validator {
    @Autowired
    private AccountDao accountDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return RecoveryEntryForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RecoveryEntryForm form = (RecoveryEntryForm) o;

        checkEmail(form, errors);
    }

    private void checkEmail(RecoveryEntryForm form, Errors errors) {
        // Check if the e-mail address is not empty.
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "NotEmpty.email");
        if (errors.hasFieldErrors("email"))
            return;

        // Check if the e-mail address has been registered.
        Account account = accountDao.findByEmail(form.getEmail()).orElse(null);
        if (null == account || !account.isActive()) {
            errors.rejectValue("email", "Active.email");
        }
    }
}
