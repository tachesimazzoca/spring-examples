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
public class QuestionResultDaoTest {
    @Autowired
    private DataSource dataSource;

    private void resetTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE questions");
        jdbcTemplate.execute("ALTER TABLE questions ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE answers");
        jdbcTemplate.execute("ALTER TABLE answers ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE accounts");
        jdbcTemplate.execute("ALTER TABLE accounts ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE account_questions");
        jdbcTemplate.execute("TRUNCATE TABLE account_answers");
    }

    @Test
    public void testSelectPublicQuestions() {
        resetTables();

        long time = System.currentTimeMillis();

        final int NUM_ACCOUNTS = 10;
        final int NUM_QUESTIONS = 5;

        Long[] accountIds = new Long[NUM_ACCOUNTS];
        AccountDao accountDao = new AccountDao(dataSource);
        for (int i = 0; i < NUM_ACCOUNTS; i++) {
            Account account = new Account();
            account.setEmail("user" + i + "@example.net");
            account.setPasswordSalt("");
            account.setPasswordHash("");
            account.setNickname("");
            account.setStatus(Account.Status.ACTIVE);
            Account savedAccount = accountDao.save(account);
            accountIds[i] = savedAccount.getId();
        }

        int numQuestions = 0;
        QuestionDao questionDao = new QuestionDao(dataSource);
        for (int i = 0; i < accountIds.length; i++) {
            for (int j = 0; j < 5; j++) {
                Question question = new Question();
                question.setAuthorId(accountIds[i]);
                question.setSubject("title" + i + "-" + j);
                question.setBody("body" + i + "-" + j);
                question.setPostedAt(new java.util.Date(time - 3600000 * j));
                question.setStatus(Question.Status.PUBLISHED);
                questionDao.save(question);
                numQuestions++;
            }
        }

        QuestionResultDao questionResultDao = new QuestionResultDao(dataSource);

        // It should adjust the maximum offset if the given offset is greater
        // than the number of results.
        int limit = 20;
        Pagination<QuestionResult> pagination = questionResultDao.selectPublicQuestions(
                numQuestions, limit, QuestionResult.OrderBy.POSTED_AT_DESC);
        assertEquals(((numQuestions - 1) / limit * limit), pagination.getOffset());
    }
}
