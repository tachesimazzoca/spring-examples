package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.junit.Assert.*;

public class AnswerDaoTest {
    private static ApplicationContext context = new ClassPathXmlApplicationContext(
            "spring/database.xml");

    private DataSource dataSource() {
        return context.getBean("testDataSource", DataSource.class);
    }

    private void resetTables(DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("TRUNCATE TABLE questions");
        jdbcTemplate.execute("ALTER TABLE questions ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    public void testSaveAndUpdateStatus() {
        DataSource ds = dataSource();
        resetTables(ds);

        long time = System.currentTimeMillis();

        AnswerDao dao = new AnswerDao(ds);
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
