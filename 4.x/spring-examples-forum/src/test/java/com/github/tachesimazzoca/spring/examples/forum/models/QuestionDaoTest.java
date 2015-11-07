package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class QuestionDaoTest {
    @Autowired
    private DataSource dataSource;

    private void resetTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE questions");
        jdbcTemplate.execute("ALTER TABLE questions ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    public void testSaveAndUpdateStatus() {
        resetTables();

        long time = System.currentTimeMillis();

        QuestionDao dao = new QuestionDao(dataSource);
        Question question = new Question();
        question.setAuthorId(2L);
        question.setStatus(Question.Status.PUBLISHED);
        question.setPostedAt(new java.util.Date(time));
        Question savedQuestion = dao.save(question);

        Long id = savedQuestion.getId();
        assertNotNull(id);
        assertEquals(2L, savedQuestion.getAuthorId().longValue());
        assertEquals(Question.Status.PUBLISHED, savedQuestion.getStatus());
        assertEquals(time, savedQuestion.getPostedAt().getTime());

        dao.updateStatus(id, Question.Status.DRAFT);
        Question updatedQuestion = dao.find(id).get();
        assertEquals(id, updatedQuestion.getId());
        assertEquals(Question.Status.DRAFT, updatedQuestion.getStatus());
    }
}
