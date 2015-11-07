package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JdbcTemplateStorageTest {
    private static ApplicationContext context = new ClassPathXmlApplicationContext(
            "spring/database.xml");

    private DataSource dataSource() {
        return context.getBean("testDataSource", DataSource.class);
    }

    private void resetTables(DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        jdbcTemplate.execute("TRUNCATE TABLE session_storage");
    }

    @Test
    public void testCreateAndRead() {
        DataSource ds = dataSource();
        resetTables(ds);

        Storage<Map<String, Object>> storage = new JdbcTemplateStorage(
                ds, "session_storage", "user-");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", 1234L);
        params.put("name", "foo");
        String key = storage.create(params);
        Map<String, Object> saved = storage.read(key).get();
        assertEquals(params, saved);

        storage.delete(key);
        assertFalse(storage.read(key).isPresent());
    }
}
