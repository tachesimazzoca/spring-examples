package com.github.tachesimazzoca.spring.examples.forum.domain.model.topic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long answerId;

    private Long questionId;

    private Long authorId;

    private String body;

    private java.util.Date postedAt;

    private AnswerStatus answerStatus;
}
