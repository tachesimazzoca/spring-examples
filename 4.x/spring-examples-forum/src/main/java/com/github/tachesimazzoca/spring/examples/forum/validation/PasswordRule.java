package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.validation.Errors;

public class PasswordRule implements Rule {
    private static final String ERROR_CODE = PasswordRule.class.getSimpleName();
    private static final String PASSWORD_PATTERN = "^.{6,}$";

    private final RegexValidator patternValidator;

    private final String field;
    private final String defaultMessage;

    public PasswordRule(String field) {
        this(field, ERROR_CODE);
    }

    public PasswordRule(String field, String defaultMessage) {
        patternValidator = new RegexValidator(PASSWORD_PATTERN, false);
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
