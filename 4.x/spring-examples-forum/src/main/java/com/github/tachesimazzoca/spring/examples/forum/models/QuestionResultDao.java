package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

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
        String where = " WHERE questions.status = 0";

        String countQuery = COUNT_QUESTION_RESULT + where;
        Long count = jdbcTemplate.queryForObject(countQuery, Long.class);
        if (null == count || 0L == count) {
            return new Pagination(Collections.EMPTY_LIST, 0, limit, 0);
        }

        int first = offset;
        if (first >= count) {
            if (count > 0)
                first = (int) ((count - 1) / limit) * limit;
            else
                first = 0;
        }
        String selectQuery = String.format("%s ORDER BY %s LIMIT %d, %d",
                SELECT_QUESTION_RESULT + where,
                orderBy.getClause(),
                first, limit);
        List<QuestionResult> results = jdbcTemplate.query(selectQuery, ROW_MAPPER);

        return new Pagination(results, first, limit, count);
    }

    public Pagination<QuestionResult> selectByAuthorId(Long authorId, int offset, int limit) {
        String where = " WHERE questions.status IN (0, 2) AND questions.author_id = ?";

        String countQuery = COUNT_QUESTION_RESULT + where;
        Long count = jdbcTemplate.queryForObject(countQuery, Long.class, authorId);
        if (null == count || 0L == count) {
            return new Pagination(Collections.EMPTY_LIST, 0, limit, 0);
        }

        int first = offset;
        if (first >= count) {
            if (count > 0)
                first = (int) ((count - 1) / limit) * limit;
            else
                first = 0;
        }
        String selectQuery = String.format("%s ORDER BY %s LIMIT %d, %d",
                SELECT_QUESTION_RESULT + where,
                QuestionResult.OrderBy.defaultValue().getClause(),
                first, limit);
        List<QuestionResult> results = jdbcTemplate.query(selectQuery, ROW_MAPPER, authorId);

        return new Pagination(results, first, limit, count);
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
