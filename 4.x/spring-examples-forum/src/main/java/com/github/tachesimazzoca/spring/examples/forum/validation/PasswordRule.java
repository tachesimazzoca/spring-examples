package com.github.tachesimazzoca.spring.examples.forum.validation;

public class PasswordRule extends PatternRule {
    private static final String ERROR_CODE = PasswordRule.class.getSimpleName();

    public PasswordRule(String field) {
        this(field, ERROR_CODE);
    }

    public PasswordRule(String field, String defaultMessage) {
        super("^.{6,}$", field, defaultMessage);
    }
}
