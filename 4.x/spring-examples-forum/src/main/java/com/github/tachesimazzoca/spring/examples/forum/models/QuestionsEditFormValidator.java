package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyRule;
import org.springframework.stereotype.Component;

@Component
public class QuestionsEditFormValidator extends FormValidator {
    public QuestionsEditFormValidator() {
        super(QuestionsEditForm.class,
                new NotEmptyRule("subject"),
                new NotEmptyRule("body"));
    }
}
