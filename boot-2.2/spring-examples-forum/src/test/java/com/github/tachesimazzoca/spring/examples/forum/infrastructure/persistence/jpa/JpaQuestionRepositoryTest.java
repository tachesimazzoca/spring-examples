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
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.Question;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.topic.QuestionStatus;

@ExtendWith(SpringExtension.class)
@Import(value = {TestApplicationConfig.class})
@DataJpaTest
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class JpaQuestionRepositoryTest {

    @Autowired
    private JpaQuestionRepository questionRepository;

    @Test
    public void testSaveAndFind() {
        Question[] questions = new Question[3];
        questions[0] = createQuestion(null, 1L, "1");
        questions[1] = createQuestion(null, 1L, "2");
        questions[2] = createQuestion(null, 1L, "3");
        for (Question x : questions) {
            questionRepository.save(x);
        }
        for (Question x : questions) {
            Optional<Question> entity = questionRepository.findById(x.getQuestionId());
            assertTrue(entity.isPresent());
            assertEquals(x, entity.get());
        }
    }

    @Test
    public void testEmptyResult() {
        assertFalse(questionRepository.findById(0L).isPresent());
    }

    @Test
    public void testFindByAuthorId() {
        Question[] questions = new Question[5];
        questions[0] = createQuestion(null, 1L, "1");
        questions[1] = createQuestion(null, 1L, "2");
        questions[2] = createQuestion(null, 2L, "3");
        questions[3] = createQuestion(null, 2L, "4");
        questions[4] = createQuestion(null, 1L, "5");
        for (Question x : questions) {
            questionRepository.save(x);
        }

        Set<Long> idSet1 = questionRepository.findAllByAuthorId(1L).stream()
                .map((x) -> x.getQuestionId()).collect(Collectors.toSet());
        assertEquals(3, idSet1.size());
        assertTrue(idSet1.contains(questions[0].getQuestionId()));
        assertTrue(idSet1.contains(questions[1].getQuestionId()));
        assertTrue(idSet1.contains(questions[4].getQuestionId()));

        Set<Long> idSet2 = questionRepository.findAllByAuthorId(2L).stream()
                .map((x) -> x.getQuestionId()).collect(Collectors.toSet());
        assertEquals(2, idSet2.size());
        assertTrue(idSet2.contains(questions[2].getQuestionId()));
        assertTrue(idSet2.contains(questions[3].getQuestionId()));
    }

    private static Question createQuestion(Long questionId, Long authorId, String tag) {
        Question entity = new Question();
        entity.setQuestionId(questionId);
        entity.setAuthorId(authorId);
        entity.setSubject(String.format("subject-%s", tag));
        entity.setBody(String.format("body-%s", tag));
        entity.setQuestionStatus(QuestionStatus.PUBLISHED);
        return entity;
    }
}
