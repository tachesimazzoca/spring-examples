package com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation;

import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.validation.Errors;

public class PatternChecker implements Checker {

    private final RegexValidator patternValidator;

    public PatternChecker(String pattern) {
        patternValidator = new RegexValidator(pattern, false);
    }

    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        return patternValidator.isValid((String) fieldValue);
    }
}
