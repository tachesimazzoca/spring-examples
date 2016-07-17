package com.github.tachesimazzoca.spring.examples.forum.validation;

public class PasswordRule extends PatternRule {
    public PasswordRule(String field) {
        this(field, PasswordRule.class.getSimpleName());
    }

    public PasswordRule(String field, String message) {
        super("^.{6,}$", field, message);
    }
}
