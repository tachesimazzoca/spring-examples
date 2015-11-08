package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

@Data
public class AnswerResult {
    private Long id;
    private Long questionId;
    private String body;
    private java.util.Date postedAt;
    private Answer.Status status;
    private Long authorId;
    private String nickname;
    private Integer sumPoints;
    private Integer positivePoints;
    private Integer negativePoints;
}
