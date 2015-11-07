package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

@Data
public class Question {
    private Long id;
    private Long authorId;
    private String subject;
    private String body;
    private java.util.Date postedAt;
    private Status status;

    public enum Status {
        PUBLISHED(0, "Published"), DELETED(1, "Deleted"), DRAFT(2, "Draft");

        private int value;
        private String label;

        private Status(int value, String label) {
            this.value = value;
            this.label = label;
        }

        public int getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        public static Status fromValue(int v) {
            for (Status s : Status.values()) {
                if (s.getValue() == v) {
                    return s;
                }
            }
            throw new IllegalArgumentException("unknown value: " + v);
        }

        public static Status fromValue(String str) {
            return fromValue(Integer.valueOf(str));
        }
    }
}
