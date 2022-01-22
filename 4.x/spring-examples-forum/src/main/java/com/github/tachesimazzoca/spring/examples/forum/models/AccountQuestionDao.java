package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

@Component
public class AccountQuestionDao {
    private final PlatformTransactionManager transactionManager;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AccountQuestionDao(DataSource dataSource) {
        transactionManager = new DataSourceTransactionManager(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void log(long accountId, long questionId, int point) {
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());

        jdbcTemplate.update(
                "DELETE FROM account_questions"
                        + " WHERE account_id = ? AND question_id = ?",
                accountId, questionId);
        jdbcTemplate.update("INSERT INTO account_questions"
                        + " (account_id, question_id, point, modified_at)"
                        + " VALUES (?, ?, ?, NOW())",
                accountId, questionId, point);

        transactionManager.commit(status);
    }

    private int sumPoints(Long questionId, String where) {
        Integer sum = jdbcTemplate.queryForObject(
                "SELECT SUM(point) FROM account_questions"
                        + " WHERE question_id = ? AND " + where,
                Integer.class, questionId);
        if (null == sum)
            return 0;
        else
            return sum.intValue();
    }

    public int sumPositivePoints(Long questionId) {
        return sumPoints(questionId, "point > 0");
    }

    public int sumNegativePoints(Long questionId) {
        return sumPoints(questionId, "point < 0");
    }

    public int getPoint(Long accountId, Long questionId) {
        Integer sum = jdbcTemplate.queryForObject(
                "SELECT SUM(point) FROM account_questions"
                        + " WHERE account_id = ? AND question_id = ?",
                Integer.class, accountId, questionId);
        if (null == sum)
            return 0;
        else
            return sum.intValue();
    }
}
