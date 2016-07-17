package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;

public class CurrentPasswordRule implements Rule {
    private static final String ERROR_CODE = CurrentPasswordRule.class.getSimpleName();

    private final String accountPropertyPath;
    private final String passwordField;
    private final String field;
    private final String defaultMessage;

    public CurrentPasswordRule(
            String accountPropertyPath,
            String passwordField,
            String field) {
        this(accountPropertyPath, passwordField, field, ERROR_CODE);
    }

    public CurrentPasswordRule(
            String accountPropertyPath,
            String passwordField,
            String field,
            String message) {
        this.accountPropertyPath = accountPropertyPath;
        this.passwordField = passwordField;
        this.field = field;
        this.defaultMessage = message;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        String password = (String) errors.getFieldValue(passwordField);
        if (null == password || password.isEmpty()) {
            return;
        }
        String currentPassword = (String) errors.getFieldValue(field);
        BeanWrapper beanWrapper = new BeanWrapperImpl(o);
        Account currentAccount = (Account) beanWrapper.getPropertyValue(accountPropertyPath);
        if (!currentAccount.isValidPassword(currentPassword)) {
            errors.rejectValue(field, ERROR_CODE, defaultMessage);
        }
    }
}
