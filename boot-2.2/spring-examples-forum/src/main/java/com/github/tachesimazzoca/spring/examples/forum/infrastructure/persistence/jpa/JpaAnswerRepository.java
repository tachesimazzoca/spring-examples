package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.Answer;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.AnswerRepository;

@Component
public class JpaAnswerRepository implements AnswerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Answer> findById(Long answerId) {
        Answer entity;
        try {
            entity = entityManager
                    .createQuery("select a from Answer a where answerId = :answerId",
                            Answer.class)
                    .setParameter("answerId", answerId).getSingleResult();
        } catch (NoResultException e) {
            entity = null;
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public List<Answer> findAllByQuestionId(Long questionId) {
        return entityManager
                .createQuery("select a from Answer a where questionId = :questionId", Answer.class)
                .setParameter("questionId", questionId).getResultList();
    }

    @Override
    public List<Answer> findAllByAuthorId(Long authorId) {
        return entityManager
                .createQuery("select a from Answer a where authorId = :authorId", Answer.class)
                .setParameter("authorId", authorId).getResultList();
    }

    @Override
    public <S extends Answer> S save(S entity) {
        if (entity.getAnswerId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
}
