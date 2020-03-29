package com.github.tachesimazzoca.spring.examples.forum.domain.model.topic;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

    Optional<Answer> findById(Long answerId);

    List<Answer> findAllByQuestionId(Long questionId);

    List<Answer> findAllByAuthorId(Long authorId);

    <S extends Answer> S save(S entity);
}
