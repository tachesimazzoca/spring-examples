package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jpa;

import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.Account;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.AccountRepository;

@Component
public class JpaAccountRepository implements AccountRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Account> findByEmail(String email) {
        Account entity;
        try {
            entity = entityManager
                    .createQuery("select a from Account a where email = :email", Account.class)
                    .setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            entity = null;
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public <S extends Account> S save(S entity) {
        if (entity.getAccountId() == null) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
}
