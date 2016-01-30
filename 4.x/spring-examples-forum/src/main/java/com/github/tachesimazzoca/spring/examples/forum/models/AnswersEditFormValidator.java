package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AnswersEditFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return AnswersEditForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "body", "NotEmpty.body");
    }
}
