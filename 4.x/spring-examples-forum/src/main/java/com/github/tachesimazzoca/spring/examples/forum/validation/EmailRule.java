package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;

public class EmailRule implements Rule {
    private final EmailValidator emailRule = EmailValidator.getInstance(false);

    private String field;
    private String message;

    public EmailRule(String field) {
        this(field, EmailRule.class.getSimpleName());
    }

    public EmailRule(String field, String message) {
        this.field = field;
        this.message = message;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        String email = (String) errors.getFieldValue(field);
        if (StringUtils.isBlank(email))
            return;
        if (!emailRule.isValid(email)) {
            errors.rejectValue(field, message);
        }
    }
}
