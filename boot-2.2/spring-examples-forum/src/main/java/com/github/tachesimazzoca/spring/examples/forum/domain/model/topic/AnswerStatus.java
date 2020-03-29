package com.github.tachesimazzoca.spring.examples.forum.domain.model.topic;

import com.github.tachesimazzoca.spring.examples.forum.domain.shared.ValueObject;

public enum AnswerStatus implements ValueObject<AnswerStatus> {

    PUBLISHED(0, "Published"), DELETED(1, "Deleted"), DRAFT(2, "Draft");

    private int value;
    private String label;

    private AnswerStatus(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static AnswerStatus of(int v) {
        for (AnswerStatus s : AnswerStatus.values()) {
            if (s.getValue() == v) {
                return s;
            }
        }
        throw new IllegalArgumentException("unknown value: " + v);
    }

    public static AnswerStatus of(String str) {
        return of(Integer.valueOf(str));
    }

    @Override
    public boolean sameValueAs(AnswerStatus other) {
        return this == other;
    }
}
