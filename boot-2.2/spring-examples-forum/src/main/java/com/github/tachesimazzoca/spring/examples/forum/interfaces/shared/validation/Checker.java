package com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation;

import org.springframework.validation.Errors;

public interface Checker {
    boolean check(Object fieldValue, Object o, Errors errors);
}
