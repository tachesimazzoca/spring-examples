package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FormValidator implements Validator {
    private Class<?> assignableClass;
    private List<Rule> rules = new ArrayList<Rule>();

    public void setAssignableClass(Class<?> assignableClass) {
        this.assignableClass = assignableClass;
    }

    public void setAssignableClass(String assignableClassName) {
        try {
            this.assignableClass = Class.forName(assignableClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addRule(String field, Checker checker) {
        rules.add(new Rule(field, checker, null));
    }

    public void addRule(String field, Checker checker, String defaultMessage) {
        rules.add(new Rule(field, checker, defaultMessage));
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(assignableClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        for (Rule rule : rules) {
            if (errors.hasFieldErrors(rule.field))
                continue;
            if (!rule.checker.check(errors.getFieldValue(rule.field), o, errors)) {
                final String code = rule.checker.getClass().getSimpleName();
                if (null == rule.defaultMessage) {
                    errors.rejectValue(rule.field, code, code);
                } else {
                    errors.rejectValue(rule.field, code, rule.defaultMessage);
                }
            }
        }
    }

    private class Rule {
        private final String field;
        private final Checker checker;
        private final String defaultMessage;

        public Rule(String field, Checker checker, String defaultMessage) {
            this.field = field;
            this.checker = checker;
            this.defaultMessage = defaultMessage;
        }
    }
}
