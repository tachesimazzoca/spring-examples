package com.github.tachesimazzoca.spring.examples.forum.storage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateStorageEngine implements StorageEngine {
    private final PlatformTransactionManager transactionManager;
    private final JdbcTemplate jdbcTemplate;
    private final String INSERT_QUERY;
    private final String UPDATE_QUERY;
    private final String DELETE_QUERY;
    private final String SELECT_QUERY;
    private final String SELECT_FOR_UPDATE_QUERY;

    public JdbcTemplateStorageEngine(DataSource ds, String table) {
        transactionManager = new DataSourceTransactionManager(ds);
        jdbcTemplate = new JdbcTemplate(ds);

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
    public Optional<InputStream> read(String key) {
        if (null == key)
            throw new NullPointerException("The parameter key must be not null.");

        List<Map<String, Object>> rowList = jdbcTemplate.queryForList(SELECT_QUERY, key);
        if (rowList.isEmpty()) {
            return Optional.empty();
        } else {
            byte[] decoded = Base64.decodeBase64((String) rowList.get(0).get("storage_value"));
            return Optional.of(new ByteArrayInputStream(decoded));
        }
    }

    @Override
    public void write(String key, InputStream value) {
        if (null == key)
            throw new NullPointerException("The parameter key must be not null.");

        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
        try {
            String encoded = Base64.encodeBase64String(IOUtils.toByteArray(value));
            List<Map<String, Object>> rowList = jdbcTemplate.queryForList(SELECT_FOR_UPDATE_QUERY, key);
            if (rowList.isEmpty()) {
                jdbcTemplate.update(INSERT_QUERY, key, encoded);
            } else {
                jdbcTemplate.update(UPDATE_QUERY, key, encoded);
            }
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
        transactionManager.commit(status);
    }

    @Override
    public void delete(String key) {
        if (null == key)
            throw new NullPointerException("The parameter key must be not null.");

        TransactionStatus status = transactionManager.getTransaction(
                new DefaultTransactionDefinition());
        try {
            jdbcTemplate.update(DELETE_QUERY, key);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        }
        transactionManager.commit(status);
    }
}
