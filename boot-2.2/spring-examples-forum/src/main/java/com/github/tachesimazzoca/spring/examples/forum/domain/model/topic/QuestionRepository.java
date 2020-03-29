package com.github.tachesimazzoca.spring.examples.forum.domain.model.topic;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository {

    Optional<Question> findById(Long questionId);

    List<Question> findAllByAuthorId(Long authorId);

    <S extends Question> S save(S entity);
}
