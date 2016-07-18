package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyChecker;
import org.springframework.stereotype.Component;

@Component
public class AnswersEditFormValidator extends FormValidator {
    public AnswersEditFormValidator() {
        setAssignableClass(AnswersEditForm.class);
        addRule("body", new NotEmptyChecker());
    }
}
