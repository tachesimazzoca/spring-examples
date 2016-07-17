package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

public class NotEmptyRule implements Rule {
    private static final String ERROR_CODE = NotEmptyRule.class.getSimpleName();

    private String field;
    private String defaultMessage;

    public NotEmptyRule(String field) {
        this(field, ERROR_CODE);
    }

    public NotEmptyRule(String field, String defaultMessage) {
        this.field = field;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        String value = (String) errors.getFieldValue(field);
        if (StringUtils.isBlank(value)) {
            errors.rejectValue(field, ERROR_CODE, defaultMessage);
        }
    }
}
