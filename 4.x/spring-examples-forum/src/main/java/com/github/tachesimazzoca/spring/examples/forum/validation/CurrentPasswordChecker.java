package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.validation.Errors;

public class CurrentPasswordChecker implements Checker {
    private final String accountPropertyPath;
    private final String passwordField;

    public CurrentPasswordChecker(
            String accountPropertyPath,
            String passwordField) {
        this.accountPropertyPath = accountPropertyPath;
        this.passwordField = passwordField;
    }

    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        String password = (String) errors.getFieldValue(passwordField);
        if (null == password || password.isEmpty()) {
            return true;
        }
        BeanWrapper beanWrapper = new BeanWrapperImpl(o);
        Account currentAccount = (Account) beanWrapper.getPropertyValue(accountPropertyPath);
        return currentAccount.isValidPassword((String) fieldValue);
    }
}
