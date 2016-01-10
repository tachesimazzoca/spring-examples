package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Component
public class AnswerResultDao {
    private static final AnswerResultRowMapper ROW_MAPPER = new AnswerResultRowMapper();

    private static final String SELECT_ANSWER_RESULT = "SELECT"
            + " answers.id AS answers_id,"
            + " answers.question_id AS answers_questions_id,"
            + " answers.body AS answers_body,"
            + " answers.posted_at AS answers_posted_at,"
            + " answers.status AS answers_status,"
            + " accounts.id AS accounts_id,"
            + " accounts.nickname AS accounts_nickname,"
            + " IFNULL((SELECT SUM(account_answers.point) FROM account_answers"
            + " WHERE account_answers.answer_id = answers.id), 0)"
            + " AS sum_points,"
            + " IFNULL((SELECT SUM(account_answers.point) FROM account_answers"
            + " WHERE account_answers.answer_id = answers.id"
            + " AND account_answers.point > 0), 0)"
            + " AS positive_points,"
            + " IFNULL((SELECT SUM(account_answers.point) FROM account_answers"
            + " WHERE account_answers.answer_id = answers.id"
            + " AND account_answers.point < 0), 0)"
            + " AS negative_points"
            + " FROM answers"
            + " LEFT JOIN accounts ON accounts.id = answers.author_id";

    private static final String COUNT_ANSWER_RESULT =
            "SELECT COUNT(*) FROM answers";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public AnswerResultDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Pagination<AnswerResult> selectByQuestionId(
            final Long questionId, int offset, int limit) {
        final String where = " WHERE answers.status = 0 AND answers.question_id = ?";
        final String countQuery = COUNT_ANSWER_RESULT + where;
        final String selectQuery = SELECT_ANSWER_RESULT + where
                + " ORDER BY sum_ponts DESC, answers.id ASC";

        return Pagination.paginate(offset, limit,
                () -> jdbcTemplate.queryForObject(countQuery, Long.class, questionId),
                (theOffset, theLimit) -> jdbcTemplate.query(
                        String.format(selectQuery + " LIMIT %d, %d", theOffset, theLimit),
                        ROW_MAPPER, questionId));
    }

    public Pagination<AnswerResult> selectByAuthorId(
            final Long authorId, int offset, int limit) {
        final String where = " WHERE answers.status IN (0, 2) AND answers.author_id = ?";
        final String countQuery = COUNT_ANSWER_RESULT + where;
        final String selectQuery = SELECT_ANSWER_RESULT + where
                + " ORDER BY sum_points DESC, answers.id ASC";

        return Pagination.paginate(offset, limit,
                () -> jdbcTemplate.queryForObject(countQuery, Long.class, authorId),
                (theOffset, theLimit) -> jdbcTemplate.query(
                        String.format(selectQuery + " LIMIT %d, %d", theOffset, theLimit),
                        ROW_MAPPER, authorId));
    }

    private static final class AnswerResultRowMapper implements RowMapper<AnswerResult> {
        @Override
        public AnswerResult mapRow(ResultSet resultSet, int i) throws SQLException {
            AnswerResult entity = new AnswerResult();
            entity.setId(resultSet.getLong("answers_id"));
            entity.setQuestionId(resultSet.getLong("answers_question_id"));
            entity.setBody(resultSet.getString("answers_body"));
            entity.setPostedAt(new java.util.Date(
                    resultSet.getTimestamp("answers_posted_at").getTime()));
            entity.setStatus(Answer.Status.fromValue(
                    resultSet.getInt("answers_status")));
            entity.setAuthorId(resultSet.getLong("accounts_id"));
            entity.setNickname(resultSet.getString("accounts_nickname"));
            entity.setSumPoints(resultSet.getInt("sum_points"));
            entity.setPositivePoints(resultSet.getInt("positive_points"));
            entity.setNegativePoints(resultSet.getInt("negative_points"));
            return entity;
        }
    }
}
