package com.github.tachesimazzoca.spring.examples.forum.domain.model.topic;

import com.github.tachesimazzoca.spring.examples.forum.domain.shared.ValueObject;

public enum QuestionStatus implements ValueObject<QuestionStatus> {

    PUBLISHED(0, "Published"), DELETED(1, "Deleted"), DRAFT(2, "Draft");

    private int value;
    private String label;

    private QuestionStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static QuestionStatus of(int v) {
        for (QuestionStatus s : QuestionStatus.values()) {
            if (s.getValue() == v) {
                return s;
            }
        }
        throw new IllegalArgumentException("unknown value: " + v);
    }

    public static QuestionStatus of(String str) {
        return of(Integer.valueOf(str));
    }

    @Override
    public boolean sameValueAs(QuestionStatus other) {
        return this == other;
    }
}
