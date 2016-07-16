package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyRule;
import org.springframework.stereotype.Component;

@Component
public class AnswersEditFormValidator extends FormValidator {
    public AnswersEditFormValidator() {
        super(AnswersEditForm.class,
                new NotEmptyRule("body"));
    }
}
