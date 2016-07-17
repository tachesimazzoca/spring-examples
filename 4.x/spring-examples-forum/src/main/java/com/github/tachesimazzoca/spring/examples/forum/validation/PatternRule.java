package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.validation.Errors;

public class PatternRule implements Rule {
    private static final String ERROR_CODE = PatternRule.class.getSimpleName();

    private final RegexValidator patternValidator;

    private final String field;
    private final String defaultMessage;

    public PatternRule(String pattern, String field) {
        this(pattern, field, ERROR_CODE);
    }

    public PatternRule(String pattern, String field, String defaultMessage) {
        patternValidator = new RegexValidator(pattern, false);
        this.field = field;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        String value = (String) errors.getFieldValue(field);
        if (StringUtils.isBlank(value))
            return;
        if (!patternValidator.isValid(value)) {
            errors.rejectValue(field, ERROR_CODE, defaultMessage);
        }
    }
}
