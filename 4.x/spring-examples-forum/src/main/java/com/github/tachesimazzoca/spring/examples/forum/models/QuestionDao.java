package com.github.tachesimazzoca.spring.examples.forum.models;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class QuestionDao extends JdbcTemplateDao<Question> {
    public static final String TABLE_NAME = "questions";
    public static final String[] COLUMNS = {"author_id", "subject", "body", "posted_at", "status"};
    public static final String GENERATED_KEY_COLUMN = "id";

    public QuestionDao(DataSource dataSource) {
        super(dataSource, TABLE_NAME, COLUMNS, GENERATED_KEY_COLUMN);
    }

    public Question save(Question entity) {
        if (null == entity.getId())
            return create(entity);
        else
            return update(entity);
    }

    public void updateStatus(final Long id, final Question.Status status) {
        getJdbcTemplate().update("UPDATE questions SET status = ? WHERE id = ?",
                status.getValue(), id);
    }

    @Override
    protected Map<String, ?> convertEntityToMap(Question entity) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", entity.getId());
        params.put("author_id", entity.getAuthorId());
        params.put("subject", entity.getSubject());
        params.put("body", entity.getBody());
        params.put("posted_at", new java.sql.Timestamp(entity.getPostedAt().getTime()));
        params.put("status", entity.getStatus().getValue());
        return params;
    }

    @Override
    public Question convertResultSetToEntity(ResultSet resultSet) throws SQLException {
        Question entity = new Question();
        entity.setId(resultSet.getLong("id"));
        entity.setAuthorId(resultSet.getLong("author_id"));
        entity.setSubject(resultSet.getString("subject"));
        entity.setBody(resultSet.getString("body"));
        entity.setPostedAt(new java.util.Date(resultSet.getTimestamp("posted_at").getTime()));
        entity.setStatus(Question.Status.fromValue(resultSet.getInt("status")));
        return entity;
    }
}
