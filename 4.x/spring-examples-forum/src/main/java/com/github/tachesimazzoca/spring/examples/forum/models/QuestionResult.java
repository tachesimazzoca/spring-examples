package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

@Data
public class QuestionResult {
    private Long id;
    private String subject;
    private String body;
    private java.util.Date postedAt;
    private Question.Status status;
    private Long authorId;
    private String nickname;
    private Integer numAnswers = 0;
    private Integer sumPoints = 0;
    private Integer positivePoints = 0;
    private Integer negativePoints = 0;

    public enum OrderBy {
        POSTED_AT_DESC("posted_at_desc",
                "questions.posted_at DESC, questions.id ASC"),
        NUM_ANSWERS_DESC("num_answers_desc",
                "num_answers DESC, questions.posted_at DESC, questions.id ASC"),
        SUM_POINTS_DESC("sum_points_desc",
                "sum_points DESC, questions.posted_at DESC, questions.id ASC");

        private String name;
        private String clause;

        private OrderBy(String name, String clause) {
            this.name = name;
            this.clause = clause;
        }

        public String getName() {
            return name;
        }

        public String getClause() {
            return clause;
        }

        public static OrderBy defaultValue() {
            return POSTED_AT_DESC;
        }

        public static OrderBy fromName(String name) {
            for (OrderBy x : OrderBy.values()) {
                if (x.getName().equals(name)) {
                    return x;
                }
            }
            return defaultValue();
        }
    }
}
