package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.validation.Errors;

public class PasswordChecker implements Checker {
    private static final String PASSWORD_PATTERN = "^.{6,}$";
    private final Checker patternChecker;
    private final boolean required;

    public PasswordChecker() {
        this(true);
    }

    public PasswordChecker(boolean required) {
        patternChecker = new PatternChecker(PASSWORD_PATTERN);
        this.required = required;
    }

    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        if (!required && ((String) fieldValue).isEmpty())
            return true;
        return patternChecker.check(fieldValue, o, errors);
    }
}
