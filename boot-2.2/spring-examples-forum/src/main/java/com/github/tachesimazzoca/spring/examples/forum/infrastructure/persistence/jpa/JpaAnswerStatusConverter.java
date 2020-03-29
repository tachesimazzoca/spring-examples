package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.AnswerStatus;

@Converter
public class JpaAnswerStatusConverter implements AttributeConverter<AnswerStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AnswerStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public AnswerStatus convertToEntityAttribute(Integer dbData) {
        return AnswerStatus.of(dbData);
    }
}
