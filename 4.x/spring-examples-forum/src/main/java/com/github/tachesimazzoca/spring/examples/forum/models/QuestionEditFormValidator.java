package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyChecker;
import org.springframework.stereotype.Component;

@Component
public class QuestionEditFormValidator extends FormValidator {
    public QuestionEditFormValidator() {
        setAssignableClass(QuestionEditForm.class);
        addRule("subject", new NotEmptyChecker());
        addRule("body", new NotEmptyChecker());
    }
}
