package com.github.tachesimazzoca.spring.examples.forum.infrastructure.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.github.tachesimazzoca.spring.examples.forum.config.TestApplicationConfig;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.Answer;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.AnswerStatus;

@ExtendWith(SpringExtension.class)
@Import(value = {TestApplicationConfig.class})
@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class JpaAnswerRepositoryTest {

    @Autowired
    private JpaAnswerRepository answerRepository;

    @Test
    public void testSaveAndFind() {
        Answer[] answers = new Answer[5];
        answers[0] = createAnswer(null, 1L, 1L, "1");
        answers[1] = createAnswer(null, 1L, 2L, "2");
        answers[2] = createAnswer(null, 2L, 3L, "3");
        answers[3] = createAnswer(null, 2L, 2L, "4");
        answers[4] = createAnswer(null, 1L, 1L, "5");
        for (Answer x : answers) {
            answerRepository.save(x);
        }
        for (Answer x : answers) {
            Optional<Answer> entity = answerRepository.findById(x.getAnswerId());
            assertTrue(entity.isPresent());
            assertEquals(x, entity.get());
        }
    }

    @Test
    public void testEmptyResult() {
        assertFalse(answerRepository.findById(0L).isPresent());
    }

    @Test
    public void testFindByQuestionId() {
        Answer[] answers = new Answer[5];
        answers[0] = createAnswer(null, 1L, 1L, "1");
        answers[1] = createAnswer(null, 1L, 2L, "2");
        answers[2] = createAnswer(null, 2L, 3L, "3");
        answers[3] = createAnswer(null, 2L, 2L, "4");
        answers[4] = createAnswer(null, 1L, 1L, "5");
        for (Answer x : answers) {
            answerRepository.save(x);
        }

        Set<Long> idSet1 = answerRepository.findAllByQuestionId(1L).stream()
                .map((x) -> x.getAnswerId()).collect(Collectors.toSet());
        assertEquals(3, idSet1.size());
        assertTrue(idSet1.contains(answers[0].getAnswerId()));
        assertTrue(idSet1.contains(answers[1].getAnswerId()));
        assertTrue(idSet1.contains(answers[4].getAnswerId()));

        Set<Long> idSet2 = answerRepository.findAllByQuestionId(2L).stream()
                .map((x) -> x.getAnswerId()).collect(Collectors.toSet());
        assertEquals(2, idSet2.size());
        assertTrue(idSet2.contains(answers[2].getAnswerId()));
        assertTrue(idSet2.contains(answers[3].getAnswerId()));
    }

    private static Answer createAnswer(Long answerId, Long questionId, Long authorId, String tag) {
        Answer entity = new Answer();
        entity.setAnswerId(answerId);
        entity.setQuestionId(questionId);
        entity.setAuthorId(authorId);
        entity.setBody(String.format("body-%s", tag));
        entity.setAnswerStatus(AnswerStatus.PUBLISHED);
        return entity;
    }
}
