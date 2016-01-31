package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class JdbcTemplateDao<T> {
    private final PlatformTransactionManager transactionManager;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<T> rowMapper;

    protected JdbcTemplateDao(DataSource dataSource,
                              String tableName,
                              String[] columns,
                              String generatedKeyColumn) {
        transactionManager = new DataSourceTransactionManager(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(tableName)
                .usingColumns(columns)
                .usingGeneratedKeyColumns(generatedKeyColumn);
        rowMapper = new InnerRowMapper();
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcInsert.getJdbcTemplate();
    }

    public RowMapper<T> getRowMapper() {
        return rowMapper;
    }

    public Optional<T> find(Number id) {
        String idColumn = jdbcInsert.getGeneratedKeyNames()[0];
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(jdbcInsert.getTableName());
        sb.append(" WHERE ");
        sb.append(idColumn);
        sb.append(" = ?");
        List<T> rowList = jdbcInsert.getJdbcTemplate().query(
                sb.toString(), rowMapper, id);
        if (!rowList.isEmpty()) {
            return Optional.of(rowList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public T create(T entity) {
        TransactionStatus transaction = transactionManager.getTransaction(
                new DefaultTransactionDefinition());

        final Map<String, ?> entityMap = convertEntityToMap(entity);
        final List<String> updateColumns = jdbcInsert.getColumnNames();

        MapSqlParameterSource params = new MapSqlParameterSource();
        for (String column : updateColumns) {
            params.addValue(column, entityMap.get(column));
        }

        try {
            Number id = jdbcInsert.executeAndReturnKey(params);
            transactionManager.commit(transaction);
            return find(id).get();
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
    }

    public T update(T entity) {
        TransactionStatus transaction = transactionManager.getTransaction(
                new DefaultTransactionDefinition());

        Map<String, ?> entityMap = convertEntityToMap(entity);
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
            if (idColumn.equals(key))
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

        try {
            jdbcInsert.getJdbcTemplate().update(
                    sql.toString(), valueList.toArray(new Object[valueList.size()]));
            transactionManager.commit(transaction);
            return find(idValue).get();
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }
    }

    abstract protected Map<String, ?> convertEntityToMap(T entity);

    abstract protected T convertResultSetToEntity(ResultSet resultSet) throws SQLException;

    private final class InnerRowMapper implements RowMapper<T> {
        @Override
        public T mapRow(ResultSet resultSet, int i) throws SQLException {
            return convertResultSetToEntity(resultSet);
        }
    }
}
