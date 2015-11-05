package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.util.ObjectSerializer;
import com.google.common.base.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JdbcTemplateStorage implements Storage<Map<String, Object>> {
    private final PlatformTransactionManager transactionManager;
    private final JdbcTemplate jdbcTemplate;
    private final String INSERT_QUERY;
    private final String UPDATE_QUERY;
    private final String DELETE_QUERY;
    private final String SELECT_QUERY;
    private final String SELECT_FOR_UPDATE_QUERY;
    private final String prefix;

    public JdbcTemplateStorage(DataSource ds, String table, String prefix) {
        transactionManager = new DataSourceTransactionManager(ds);
        jdbcTemplate = new JdbcTemplate(ds);
        this.prefix = prefix;

        INSERT_QUERY = "INSERT INTO " + table
                + " (storage_key, storage_value, storage_timestamp)"
                + " VALUES (?, ?, NOW())";

        UPDATE_QUERY = "UPDATE " + table
                + " SET storage_value = ?, storage_timestamp = NOW()"
                + " WHERE storage_key = ?";

        DELETE_QUERY = "DELETE FROM " + table + " WHERE storage_key = ?";

        SELECT_QUERY = "SELECT storage_value FROM " + table
                + " WHERE storage_key = ?";

        SELECT_FOR_UPDATE_QUERY = "SELECT storage_value FROM " + table
                + " WHERE storage_key = ? FOR UPDATE";
    }

    @Override
    public String create(Map<String, Object> value) {
        String key = prefix + UUID.randomUUID().toString();
        write(key, value);
        return key;
    }

    @Override
    public Optional<Map<String, Object>> read(String key) {
        List<Map<String, Object>> rowList = jdbcTemplate.queryForList(SELECT_QUERY, key);
        Map<String, Object> v = null;
        if (!rowList.isEmpty()) {
            try {
                String encoded = (String) rowList.get(0).get("storage_value");
                v = (Map<String, Object>) ObjectSerializer.BASE64.deserialize(encoded, Map.class);
            } catch (Exception e) {
                // fail gracefully if the storage value is corrupted or type
                // mismatch.
                v = null;
            }
        }
        if (v != null)
            return Optional.of(v);
        else
            return Optional.absent();
    }

    @Override
    public void write(String key, Map<String, Object> value) {
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
        String encoded = ObjectSerializer.BASE64.serialize(value);
        List<Map<String, Object>> rowList = jdbcTemplate.queryForList(SELECT_FOR_UPDATE_QUERY, key);
        if (rowList.isEmpty()) {
            jdbcTemplate.update(INSERT_QUERY, key, encoded);
        } else {
            jdbcTemplate.update(UPDATE_QUERY, key, encoded);
        }
        transactionManager.commit(status);
    }

    @Override
    public void delete(String key) {
        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
        jdbcTemplate.update(DELETE_QUERY, key);
        transactionManager.commit(status);
    }
}
