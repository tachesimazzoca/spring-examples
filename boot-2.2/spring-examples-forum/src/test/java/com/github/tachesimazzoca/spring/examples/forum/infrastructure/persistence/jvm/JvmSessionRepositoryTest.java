package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jvm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.Session;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.session.SessionRepository;

public class JvmSessionRepositoryTest {
    @Test
    public void testCreate() {
        SessionRepository sessionRepository = new JvmSessionRepository();
        Session expected = Session.create();
        sessionRepository.create(expected);
        Session actual = sessionRepository.read(expected.getSessionId()).get();
        assertEquals(expected, actual);
    }

    @Test
    public void testUpdate() {
        SessionRepository sessionRepository = new JvmSessionRepository();
        Session s = Session.create();
        sessionRepository.create(s);
        assertTrue(sessionRepository.read(s.getSessionId()).get().getValueMap().isEmpty());
        s.getValueMap().put("email", "foo@example.net");
        sessionRepository.update(s);
        assertEquals(1, sessionRepository.read(s.getSessionId()).get().getValueMap().size());
        assertEquals("foo@example.net",
                sessionRepository.read(s.getSessionId()).get().getValueMap().get("email"));
    }

    @Test
    public void testDelete() {
        SessionRepository sessionRepository = new JvmSessionRepository();
        Session s1 = Session.create();
        Session s2 = Session.create();
        sessionRepository.create(s1);
        sessionRepository.create(s2);
        sessionRepository.delete(s1.getSessionId());
        assertFalse(sessionRepository.read(s1.getSessionId()).isPresent());
        assertTrue(sessionRepository.read(s2.getSessionId()).isPresent());
    }

    @Test
    public void testSessionIdConflict() {
        SessionRepository sessionRepository = new JvmSessionRepository();
        Session s = Session.create();
        sessionRepository.create(s);
        assertThrows(IllegalArgumentException.class, () -> {
            sessionRepository.create(s);
        });
    }
}
