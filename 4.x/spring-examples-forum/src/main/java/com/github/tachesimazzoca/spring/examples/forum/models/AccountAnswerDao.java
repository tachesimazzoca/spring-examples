package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

public class AccountAnswerDao {
    private final PlatformTransactionManager transactionManager;
    private final JdbcTemplate jdbcTemplate;

    public AccountAnswerDao(DataSource dataSource) {
        transactionManager = new DataSourceTransactionManager(dataSource);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void log(long accountId, long answerId, int point) {
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());

        jdbcTemplate.update(
                "DELETE account_answers"
                        + " WHERE account_id = ? AND answer_id = ?",
                accountId, answerId);
        jdbcTemplate.update("INSERT INTO account_answers"
                        + " (account_id, answer_id, point, modified_at)"
                        + " VALUES (?, ?, ?, NOW())",
                accountId, answerId, point);

        transactionManager.commit(status);
    }

    private int sumPoints(Long answerId, String where) {
        Integer sum = jdbcTemplate.queryForObject(
                "SELECT SUM(point) FROM account_answers"
                        + " WHERE answer_id = ? AND " + where,
                Integer.class, answerId);
        if (null == sum)
            return 0;
        else
            return sum.intValue();
    }

    public int sumPositivePoints(Long answerId) {
        return sumPoints(answerId, "point > 0");
    }

    public int sumNegativePoints(Long answerId) {
        return sumPoints(answerId, "point < 0");
    }

    public int getPoint(Long accountId, Long answerId) {
        Integer sum = jdbcTemplate.queryForObject(
                "SELECT SUM(point) FROM account_answers"
                        + " WHERE account_id = ? AND answer_id = ?",
                Integer.class, accountId, answerId);
        if (null == sum)
            return 0;
        else
            return sum.intValue();
    }
}
