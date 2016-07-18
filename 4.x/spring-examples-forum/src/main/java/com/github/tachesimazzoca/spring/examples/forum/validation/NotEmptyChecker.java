package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;

import java.util.List;

public class NotEmptyChecker implements Checker {
    @Override
    public boolean check(Object fieldValue, Object o, Errors errors) {
        if (fieldValue instanceof String) {
            return !StringUtils.isBlank((String) fieldValue);
        } else if (fieldValue instanceof List) {
            return !((List<?>) fieldValue).isEmpty();
        } else {
            return null != fieldValue;
        }
    }
}
