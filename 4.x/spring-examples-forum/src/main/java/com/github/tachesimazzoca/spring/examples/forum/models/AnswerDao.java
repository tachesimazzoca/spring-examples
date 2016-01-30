package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AnswerDao extends JdbcTemplateDao<Answer> {
    public static final String TABLE_NAME = "answers";
    public static final String[] COLUMNS = {
            "question_id", "author_id", "body", "posted_at", "status"};
    public static final String GENERATED_KEY_COLUMN = "id";

    @Autowired
    public AnswerDao(DataSource dataSource) {
        super(dataSource, TABLE_NAME, COLUMNS, GENERATED_KEY_COLUMN);
    }

    public Answer save(Answer entity) {
        if (null == entity.getId())
            return create(entity);
        else
            return update(entity);
    }

    public void updateStatus(Long id, Answer.Status status) {
        getJdbcTemplate().update("UPDATE answers SET status = ? WHERE id = ?",
                status.getValue(), id);
    }

    @Override
    protected Map<String, ?> convertEntityToMap(Answer entity) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", entity.getId());
        params.put("question_id", entity.getQuestionId());
        params.put("author_id", entity.getAuthorId());
        params.put("body", entity.getBody());
        params.put("posted_at", new java.sql.Timestamp(entity.getPostedAt().getTime()));
        params.put("status", entity.getStatus().getValue());
        return params;
    }

    @Override
    public Answer convertResultSetToEntity(ResultSet resultSet) throws SQLException {
        Answer entity = new Answer();
        entity.setId(resultSet.getLong("id"));
        entity.setQuestionId(resultSet.getLong("question_id"));
        entity.setAuthorId(resultSet.getLong("author_id"));
        entity.setBody(resultSet.getString("body"));
        entity.setPostedAt(new java.util.Date(resultSet.getTimestamp("posted_at").getTime()));
        entity.setStatus(Answer.Status.fromValue(resultSet.getInt("status")));
        return entity;
    }
}
