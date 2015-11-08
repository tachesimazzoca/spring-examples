package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionResultDao {
    private static final QuestionResultRowMapper ROW_MAPPER = new QuestionResultRowMapper();

    private static final String SELECT_QUESTION_RESULT = "SELECT"
            + " questions.id AS questions_id,"
            + " questions.subject AS questions_subject,"
            + " questions.body AS questions_body,"
            + " questions.posted_at AS questions_posted_at,"
            + " questions.status AS questions_status,"
            + " accounts.id AS accounts_id,"
            + " accounts.nickname AS accounts_nickname,"
            + " IFNULL((SELECT COUNT(*) FROM answers"
            + " WHERE answers.question_id = questions.id AND answers.status = 0), 0)"
            + " AS num_answers,"
            + " IFNULL((SELECT SUM(account_questions.point) FROM account_questions"
            + " WHERE account_questions.question_id = questions.id), 0)"
            + " AS sum_points,"
            + " IFNULL((SELECT SUM(account_questions.point) FROM account_questions"
            + " WHERE account_questions.question_id = questions.id"
            + " AND account_questions.point > 0), 0)"
            + " AS positive_points,"
            + " IFNULL((SELECT SUM(account_questions.point) FROM account_questions"
            + " WHERE account_questions.question_id = questions.id"
            + " AND account_questions.point < 0), 0)"
            + " AS negative_points"
            + " FROM questions"
            + " LEFT JOIN accounts ON accounts.id = questions.author_id";

    private static final String COUNT_QUESTION_RESULT =
            "SELECT COUNT(*) FROM questions";

    private JdbcTemplate jdbcTemplate;

    public QuestionResultDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Pagination<QuestionResult> selectPublicQuestions(
            int offset, int limit, QuestionResult.OrderBy orderBy) {
        final String where = " WHERE questions.status = 0";
        final String countQuery = COUNT_QUESTION_RESULT + where;
        final String selectQuery = SELECT_QUESTION_RESULT + where
                + " ORDER BY " + orderBy.getClause();

        return Pagination.paginate(offset, limit,
                () -> jdbcTemplate.queryForObject(countQuery, Long.class),
                (theOffset, theLimit) -> jdbcTemplate.query(
                        String.format(selectQuery + " LIMIT %d, %d", theOffset, theLimit),
                        ROW_MAPPER));
    }

    public Pagination<QuestionResult> selectByAuthorId(final Long authorId, int offset, int limit) {
        final String where = " WHERE questions.status IN (0, 2) AND questions.author_id = ?";
        final String countQuery = COUNT_QUESTION_RESULT + where;
        final String selectQuery = SELECT_QUESTION_RESULT + where
                + " ORDER BY " + QuestionResult.OrderBy.defaultValue().getClause();

        return Pagination.paginate(offset, limit,
                () -> jdbcTemplate.queryForObject(countQuery, Long.class, authorId),
                (theOffset, theLimit) -> jdbcTemplate.query(
                        String.format(selectQuery + " LIMIT %d, %d", theOffset, theLimit),
                        ROW_MAPPER, authorId));
    }

    private static final class QuestionResultRowMapper implements RowMapper<QuestionResult> {
        @Override
        public QuestionResult mapRow(ResultSet resultSet, int i) throws SQLException {
            QuestionResult entity = new QuestionResult();
            entity.setId(resultSet.getLong("questions_id"));
            entity.setSubject(resultSet.getString("questions_subject"));
            entity.setBody(resultSet.getString("questions_body"));
            entity.setPostedAt(new java.util.Date(
                    resultSet.getTimestamp("questions_posted_at").getTime()));
            entity.setStatus(Question.Status.fromValue(
                    resultSet.getInt("questions_status")));
            entity.setAuthorId(resultSet.getLong("accounts_id"));
            entity.setNickname(resultSet.getString("accounts_nickname"));
            entity.setNumAnswers(resultSet.getInt("num_answers"));
            entity.setSumPoints(resultSet.getInt("sum_points"));
            entity.setPositivePoints(resultSet.getInt("positive_points"));
            entity.setNegativePoints(resultSet.getInt("negative_points"));
            return entity;
        }
    }
}
