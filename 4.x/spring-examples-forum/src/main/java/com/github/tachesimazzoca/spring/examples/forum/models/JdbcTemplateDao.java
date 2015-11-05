package com.github.tachesimazzoca.spring.examples.forum.models;

import com.google.common.base.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class JdbcTemplateDao<T> {
    private final PlatformTransactionManager transactionManager;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<T> rowMapper;

    protected JdbcTemplateDao(PlatformTransactionManager transactionManager,
                              JdbcTemplate jdbcTemplate,
                              SimpleJdbcInsert jdbcInsert,
                              RowMapper<T> rowMapper) {
        this.transactionManager = transactionManager;
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = jdbcInsert;
        this.rowMapper = rowMapper;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public Optional<T> find(Number id) {
        String idColumn = jdbcInsert.getGeneratedKeyNames()[0];
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(jdbcInsert.getTableName());
        sb.append(" WHERE ");
        sb.append(idColumn);
        sb.append(" = ?");
        List<T> rowList = jdbcTemplate.query(sb.toString(), rowMapper, id);
        if (rowList.isEmpty()) {
            return Optional.absent();
        } else {
            return Optional.of(rowList.get(0));
        }
    }

    public T create(T entity) {
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());

        final Map<String, ?> entityMap = convertToMap(entity);
        final List<String> updateColumns = jdbcInsert.getColumnNames();

        MapSqlParameterSource params = new MapSqlParameterSource();
        for (String column : updateColumns) {
            params.addValue(column, entityMap.get(column));
        }

        Number id = jdbcInsert.executeAndReturnKey(params);
        transactionManager.commit(status);

        return find(id).get();
    }

    public T update(T entity) {
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());

        Map<String, ?> entityMap = convertToMap(entity);
        String idColumn = jdbcInsert.getGeneratedKeyNames()[0];
        Number idValue = (Number) entityMap.get(idColumn);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(jdbcInsert.getTableName());
        sql.append(" SET ");

        List<Object> valueList = new ArrayList<Object>();
        int n = 0;
        for (Map.Entry<String, ?> kv : entityMap.entrySet()) {
            String key = kv.getKey();
            if (idColumn == key)
                continue;
            if (n > 0)
                sql.append(", ");
            sql.append(kv.getKey());
            sql.append(" = ?");
            valueList.add(kv.getValue());
            n++;
        }
        sql.append(" WHERE ");
        sql.append(idColumn);
        sql.append(" = ?");
        valueList.add(idValue);

        jdbcTemplate.update(sql.toString(), valueList.toArray(new Object[valueList.size()]));
        transactionManager.commit(status);

        return find(idValue).get();
    }

    abstract protected Map<String, ?> convertToMap(T entity);
}
