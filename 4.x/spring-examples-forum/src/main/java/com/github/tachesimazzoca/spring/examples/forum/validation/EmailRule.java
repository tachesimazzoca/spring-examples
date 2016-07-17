package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;

public class EmailRule implements Rule {
    private static final String ERROR_CODE = EmailRule.class.getSimpleName();

    private final EmailValidator emailRule = EmailValidator.getInstance(false);

    private String field;
    private String defaultMessage;

    public EmailRule(String field) {
        this(field, ERROR_CODE);
    }

    public EmailRule(String field, String defaultMessage) {
        this.field = field;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        String email = (String) errors.getFieldValue(field);
        if (StringUtils.isBlank(email))
            return;
        if (!emailRule.isValid(email)) {
            errors.rejectValue(field, ERROR_CODE, defaultMessage);
        }
    }
}
