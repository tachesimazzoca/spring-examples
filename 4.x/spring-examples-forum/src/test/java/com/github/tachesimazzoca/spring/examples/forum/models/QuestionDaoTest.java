package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static org.junit.Assert.*;

public class QuestionDaoTest {
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

        QuestionDao dao = new QuestionDao(ds);
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
        Question updatedAccount = dao.find(id).get();
        assertEquals(id, updatedAccount.getId());
        assertEquals(Question.Status.DRAFT, updatedAccount.getStatus());
    }
}
