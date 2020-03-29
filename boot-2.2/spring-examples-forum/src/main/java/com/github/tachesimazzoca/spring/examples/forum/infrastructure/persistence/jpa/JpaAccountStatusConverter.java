package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.AccountStatus;

@Converter
public class JpaAccountStatusConverter implements AttributeConverter<AccountStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AccountStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public AccountStatus convertToEntityAttribute(Integer dbData) {
        return AccountStatus.of(dbData);
    }
}
