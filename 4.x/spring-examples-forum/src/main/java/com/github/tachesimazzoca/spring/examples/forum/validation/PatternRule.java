package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.validation.Errors;

public class PatternRule implements Rule {
    private final RegexValidator patternValidator;

    private final String field;
    private final String message;

    public PatternRule(String pattern, String field) {
        this(pattern, field, PatternRule.class.getSimpleName());
    }

    public PatternRule(String pattern, String field, String message) {
        patternValidator = new RegexValidator(pattern, false);
        this.field = field;
        this.message = message;
    }

    @Override
    public void check(Object o, Errors errors) {
        if (errors.hasFieldErrors(field))
            return;
        String value = (String) errors.getFieldValue(field);
        if (StringUtils.isBlank(value))
            return;
        if (!patternValidator.isValid(value)) {
            errors.rejectValue(field, message);
        }
    }
}
