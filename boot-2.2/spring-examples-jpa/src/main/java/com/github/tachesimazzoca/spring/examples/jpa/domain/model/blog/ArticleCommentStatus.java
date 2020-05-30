package com.github.tachesimazzoca.spring.examples.jpa.domain.model.blog;

import javax.persistence.AttributeConverter;

public enum ArticleCommentStatus {

    POSTED(0), ALLOWED(1), REJECTED(2);

    private int value;

    private ArticleCommentStatus(int value) {
            this.value = value;
        }

    public int getValue() {
        return value;
    }

    public static ArticleCommentStatus fromValue(int v) {
        for (ArticleCommentStatus s : ArticleCommentStatus.values()) {
            if (s.getValue() == v) {
                return s;
            }
        }
        throw new IllegalArgumentException("unknown value: " + v);
    }

    public static class Converter implements
            AttributeConverter<ArticleCommentStatus, Integer> {
        @Override
        public Integer convertToDatabaseColumn(ArticleCommentStatus status) {
            return status.getValue();
        }

        @Override
        public ArticleCommentStatus convertToEntityAttribute(Integer value) {
            return ArticleCommentStatus.fromValue(value);
        }
    }
}
