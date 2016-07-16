package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

public class EqualRule implements Rule {
    private final String targetField;
    private final String field;
    private final String message;

    public EqualRule(String targetField, String field) {
        this(targetField, field, EqualRule.class.getSimpleName());
    }

    public EqualRule(String targetField, String field, String message) {
        this.targetField = targetField;
        this.field = field;
        this.message = message;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;

        String a = (String) errors.getFieldValue(targetField);
        if (StringUtils.isBlank(a))
            return;

        String b = (String) errors.getFieldValue(field);
        if (!a.equals(b)) {
            errors.rejectValue(field, message);
        }
    }
}
