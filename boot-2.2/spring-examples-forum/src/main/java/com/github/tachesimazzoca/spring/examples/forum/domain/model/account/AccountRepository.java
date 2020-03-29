package com.github.tachesimazzoca.spring.examples.forum.domain.model.account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findByEmail(String email);

    <S extends Account> S save(S entity);
}
