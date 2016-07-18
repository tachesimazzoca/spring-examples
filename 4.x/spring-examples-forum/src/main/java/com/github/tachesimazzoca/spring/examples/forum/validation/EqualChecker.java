package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.validation.Errors;

public class EqualChecker implements Checker {
    private final String targetField;

    public EqualChecker(String targetField) {
        this.targetField = targetField;
    }

    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        String a = (String) errors.getFieldValue(targetField);
        if (null == a)
            a = "";
        String b = String.valueOf(fieldValue);
        if (null == b)
            b = "";
        return a.equals(b);
    }
}
