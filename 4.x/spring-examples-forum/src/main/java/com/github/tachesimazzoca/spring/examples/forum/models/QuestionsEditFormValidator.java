package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyChecker;
import org.springframework.stereotype.Component;

@Component
public class QuestionsEditFormValidator extends FormValidator {
    public QuestionsEditFormValidator() {
        setAssignableClass(QuestionsEditForm.class);
        addRule("subject", new NotEmptyChecker());
        addRule("body", new NotEmptyChecker());
    }
}
