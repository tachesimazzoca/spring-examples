package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.Question;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.QuestionRepository;

@Component
public class JpaQuestionRepository implements QuestionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Question> findById(Long questionId) {
        Question entity;
        try {
            entity = entityManager
                    .createQuery("select a from Question a where questionId = :questionId",
                            Question.class)
                    .setParameter("questionId", questionId).getSingleResult();
        } catch (NoResultException e) {
            entity = null;
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public List<Question> findAllByAuthorId(Long authorId) {
        return entityManager
                .createQuery("select a from Question a where authorId = :authorId", Question.class)
                .setParameter("authorId", authorId).getResultList();
    }

    @Override
    public <S extends Question> S save(S entity) {
        if (entity.getQuestionId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
}
