package com.github.tachesimazzoca.spring.examples.forum.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class JdbcTemplateStorageTest {
    @Autowired
    private DataSource dataSource;

    private void resetTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE session_storage");
    }

    @Test
    public void testCreateAndRead() {
        resetTables();

        Storage<Map<String, Object>> storage = new JdbcTemplateStorage(
                dataSource, "session_storage", "user-");

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
