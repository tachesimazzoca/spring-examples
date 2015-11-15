package com.github.tachesimazzoca.spring.examples.forum.storage;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class JdbcTemplateStorageEngineTest {
    @Autowired
    private DataSource dataSource;

    private void resetTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE session_storage");
    }

    @Test
    public void testWriteAndRead() throws IOException {
        resetTables();

        StorageEngine engine = new JdbcTemplateStorageEngine(dataSource, "session_storage");

        final String key = "a";
        final String data = "foo=bar&baz=qux";
        engine.write(key, IOUtils.toInputStream(data));
        assertEquals(data, IOUtils.toString(engine.read(key).get()));

        engine.delete(key);
        assertFalse(engine.read(key).isPresent());
    }
}
