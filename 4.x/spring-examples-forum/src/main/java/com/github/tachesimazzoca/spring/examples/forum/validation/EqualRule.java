package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.validation.Errors;

public class EqualRule implements Rule {
    private static final String ERROR_CODE = EqualRule.class.getSimpleName();

    private final String targetField;
    private final String field;
    private final String defaultMessage;

    public EqualRule(String targetField, String field) {
        this(targetField, field, ERROR_CODE);
    }

    public EqualRule(String targetField, String field, String defaultMessage) {
        this.targetField = targetField;
        this.field = field;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;

        String a = (String) errors.getFieldValue(targetField);
        if (null == a)
            a = "";
        String b = (String) errors.getFieldValue(field);
        if (null == b)
            b = "";
        if (!a.equals(b)) {
            errors.rejectValue(field, ERROR_CODE, defaultMessage);
        }
    }
}
