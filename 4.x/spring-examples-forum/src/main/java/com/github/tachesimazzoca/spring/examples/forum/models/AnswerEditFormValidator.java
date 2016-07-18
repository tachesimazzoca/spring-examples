package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyChecker;
import org.springframework.stereotype.Component;

@Component
public class AnswerEditFormValidator extends FormValidator {
    public AnswerEditFormValidator() {
        setAssignableClass(AnswerEditForm.class);
        addRule("body", new NotEmptyChecker());
    }
}
