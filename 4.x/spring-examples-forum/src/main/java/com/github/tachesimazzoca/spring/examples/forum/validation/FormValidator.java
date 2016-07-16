package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FormValidator implements Validator {
    private Class<?> assignableClass;
    private Rule[] rules;

    public FormValidator(String assignableClassName, Rule... rules) {
        try {
            this.assignableClass = Class.forName(assignableClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public FormValidator(Class<?> assignableClass, Rule... rules) {
        this.assignableClass = assignableClass;
        this.rules = rules;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(assignableClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        for (Rule rule : rules) {
            rule.check(o, errors);
        }
    }
}
