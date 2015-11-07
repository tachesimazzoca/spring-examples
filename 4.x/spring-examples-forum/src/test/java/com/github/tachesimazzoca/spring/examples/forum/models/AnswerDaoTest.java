package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class AnswerDaoTest {
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

        AnswerDao dao = new AnswerDao(dataSource);
        Answer answer = new Answer();
        answer.setQuestionId(2L);
        answer.setAuthorId(3L);
        answer.setStatus(Answer.Status.PUBLISHED);
        answer.setPostedAt(new java.util.Date(time));
        Answer savedAnswer = dao.save(answer);

        Long id = savedAnswer.getId();
        assertNotNull(id);
        assertEquals(2L, savedAnswer.getQuestionId().longValue());
        assertEquals(3L, savedAnswer.getAuthorId().longValue());
        assertEquals(Answer.Status.PUBLISHED, savedAnswer.getStatus());
        assertEquals(time, savedAnswer.getPostedAt().getTime());

        dao.updateStatus(id, Answer.Status.DRAFT);
        Answer updatedAnswer = dao.find(id).get();
        assertEquals(id, updatedAnswer.getId());
        assertEquals(Answer.Status.DRAFT, updatedAnswer.getStatus());
    }
}
