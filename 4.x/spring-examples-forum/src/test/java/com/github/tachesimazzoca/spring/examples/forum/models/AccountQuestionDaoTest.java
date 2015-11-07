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
public class AccountQuestionDaoTest {
    @Autowired
    private DataSource dataSource;

    private void resetTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE account_questions");
    }

    @Test
    public void testLogAndGetPoint() {
        resetTables();

        AccountQuestionDao dao = new AccountQuestionDao(dataSource);
        dao.log(10001L, 20001L, 5);
        assertEquals(5, dao.getPoint(10001L, 20001L));
        assertEquals(5, dao.sumPositivePoints(20001L));
        assertEquals(0, dao.sumNegativePoints(20001L));

        dao.log(10002L, 20001L, -3);
        assertEquals(-3, dao.getPoint(10002L, 20001L));
        assertEquals(5, dao.sumPositivePoints(20001L));
        assertEquals(-3, dao.sumNegativePoints(20001L));

        dao.log(10003L, 20001L, 1);
        assertEquals(1, dao.getPoint(10003L, 20001L));
        assertEquals(6, dao.sumPositivePoints(20001L));
        assertEquals(-3, dao.sumNegativePoints(20001L));

        dao.log(10001L, 20002L, 2);
        assertEquals(2, dao.getPoint(10001L, 20002L));
        assertEquals(2, dao.sumPositivePoints(20002L));
        assertEquals(0, dao.sumNegativePoints(20002L));
        assertEquals(6, dao.sumPositivePoints(20001L));
        assertEquals(-3, dao.sumNegativePoints(20001L));

        dao.log(10002L, 20002L, -10);
        assertEquals(-10, dao.getPoint(10002L, 20002L));
        assertEquals(2, dao.sumPositivePoints(20002L));
        assertEquals(-10, dao.sumNegativePoints(20002L));
        assertEquals(6, dao.sumPositivePoints(20001L));
        assertEquals(-3, dao.sumNegativePoints(20001L));

        dao.log(10003L, 20002L, -2);
        assertEquals(-2, dao.getPoint(10003L, 20002L));
        assertEquals(2, dao.sumPositivePoints(20002L));
        assertEquals(-12, dao.sumNegativePoints(20002L));
        assertEquals(6, dao.sumPositivePoints(20001L));
        assertEquals(-3, dao.sumNegativePoints(20001L));
    }
}
