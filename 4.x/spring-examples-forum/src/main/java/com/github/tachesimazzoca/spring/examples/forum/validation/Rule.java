package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.validation.Errors;

public interface Rule {
    void check(Object object, Errors errors);
}
