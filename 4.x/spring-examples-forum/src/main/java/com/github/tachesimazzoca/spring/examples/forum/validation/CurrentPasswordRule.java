package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;

public class CurrentPasswordRule implements Rule {
    private final String accountPropertyPath;
    private final String passwordField;
    private final String field;
    private final String message;

    public CurrentPasswordRule(
            String accountPropertyPath,
            String passwordField,
            String field) {
        this(accountPropertyPath, passwordField, field,
                CurrentPasswordRule.class.getSimpleName());
    }

    public CurrentPasswordRule(
            String accountPropertyPath,
            String passwordField,
            String field,
            String message) {
        this.accountPropertyPath = accountPropertyPath;
        this.passwordField = passwordField;
        this.field = field;
        this.message = message;
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
            errors.rejectValue(field, message);
        }
    }
}
