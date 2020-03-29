package com.github.tachesimazzoca.spring.examples.forum.domain.model.topic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.github.tachesimazzoca.spring.examples.forum.domain.shared.Identifiable;
import lombok.Data;

@Data
@Entity
@Table(name = "question")
public class Question implements Identifiable<Question> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;

    private Long authorId;

    private String subject;

    private String body;

    private java.util.Date postedAt;

    private QuestionStatus questionStatus;

    public boolean sameIdentityAs(Question other) {
        return other != null && questionId.equals(other.getQuestionId());
    }
}
