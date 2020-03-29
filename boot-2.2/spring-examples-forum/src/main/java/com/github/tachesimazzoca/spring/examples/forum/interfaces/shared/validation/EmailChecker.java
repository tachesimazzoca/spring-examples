package com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;

public class EmailChecker implements Checker {

    private final EmailValidator emailRule = EmailValidator.getInstance(false);

    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        return emailRule.isValid((String) fieldValue);
    }
}