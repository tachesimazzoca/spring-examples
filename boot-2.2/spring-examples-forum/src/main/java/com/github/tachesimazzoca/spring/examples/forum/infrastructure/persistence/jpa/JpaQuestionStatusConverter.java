package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.QuestionStatus;

@Converter
public class JpaQuestionStatusConverter implements AttributeConverter<QuestionStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(QuestionStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public QuestionStatus convertToEntityAttribute(Integer dbData) {
        return QuestionStatus.of(dbData);
    }
}
