package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class NotEmptyRule implements Rule {
    private String field;
    private String message;

    public NotEmptyRule(String field) {
        this(field, NotEmptyRule.class.getSimpleName());
    }

    public NotEmptyRule(String field, String message) {
        this.field = field;
        this.message = message;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, message);
    }
}
