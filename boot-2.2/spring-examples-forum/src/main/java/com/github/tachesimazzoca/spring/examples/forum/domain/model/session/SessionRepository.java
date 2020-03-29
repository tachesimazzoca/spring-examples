package com.github.tachesimazzoca.spring.examples.forum.domain.model.session;

import java.util.Optional;

public interface SessionRepository {

    void create(Session session);

    Optional<Session> read(String sessionId);

    void update(Session session);

    void delete(String sessionId);

    void gc(long lifetime);
}
