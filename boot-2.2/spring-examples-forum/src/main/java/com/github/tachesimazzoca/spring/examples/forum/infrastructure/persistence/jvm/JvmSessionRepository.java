package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jvm;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.Session;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.SessionRepository;

@Component
public class JvmSessionRepository implements SessionRepository {

    private static final int STORAGE_SIZE_MAX = 100;

    private static final Map<String, String> storage = new ConcurrentHashMap<>();

    @Override
    public void create(Session session) {
        synchronized (storage) {
            if (storage.size() > STORAGE_SIZE_MAX) {
                // Clear all the entries to avoid OutOfMemory error
                storage.clear();
            }
            String v = storage.get(session.getSessionId());
            if (v != null) {
                throw new IllegalArgumentException("The sessionId has been created");
            }
            storage.put(session.getSessionId(), session.encodeValueMap());
        }
    }

    @Override
    public Optional<Session> read(String sessionId) {
        String v = storage.get(sessionId);
        if (v != null) {
            Session s = Session.create(sessionId);
            s.decodeValueMap(v);
            return Optional.of(s);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void update(Session session) {
        storage.put(session.getSessionId(), session.encodeValueMap());
    }

    @Override
    public void delete(String sessionId) {
        storage.remove(sessionId);
    }

    @Override
    public void gc(long lifetime) {
        // TODO: use lifetime
        storage.clear();
    }
}
