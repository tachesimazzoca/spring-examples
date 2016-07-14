package com.github.tachesimazzoca.spring.examples.forum.validation;

import org.springframework.validation.Errors;

import java.util.List;

public interface Rule {
    void check(String key, List<String> values, FormValueConverter form, Errors errors);
}
