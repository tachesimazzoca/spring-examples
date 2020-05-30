package com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog;

import javax.persistence.AttributeConverter;

public enum ArticleStatus {
    DRAFT(0), PUBLISHED(1);

    private int value;

    private ArticleStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ArticleStatus fromValue(int v) {
        for (ArticleStatus s : ArticleStatus.values()) {
            if (s.getValue() == v) {
                return s;
            }
        }
        throw new IllegalArgumentException("unknown value: " + v);
    }

    public static class Converter implements AttributeConverter<ArticleStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(ArticleStatus status) {
            return status.getValue();
        }

        @Override
        public ArticleStatus convertToEntityAttribute(Integer value) {
            return ArticleStatus.fromValue(value);
        }
    }
}