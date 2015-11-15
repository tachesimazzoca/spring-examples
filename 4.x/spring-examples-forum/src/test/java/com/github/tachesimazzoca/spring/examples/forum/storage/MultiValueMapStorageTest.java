package com.github.tachesimazzoca.spring.examples.forum.storage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class MultiValueMapStorageTest {
    @Autowired
    private DataSource dataSource;

    private void resetTables() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE TABLE session_storage");
    }

    @Test
    public void testCreate() throws IOException {
        resetTables();

        StorageEngine engine = new JdbcTemplateStorageEngine(dataSource, "session_storage");
        MultiValueMapStorage storage = new MultiValueMapStorage(engine, "test-");

        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
        valueMap.add("foo", "bar1");
        valueMap.add("foo", "bar2");
        valueMap.add("baz", "qux&key=value");
        valueMap.add("&#a", "=== /test!");
        String key = storage.create(valueMap);

        Optional<MultiValueMap<String, String>> valueMapOpt = storage.read(key);
        assertTrue(valueMapOpt.isPresent());

        MultiValueMap<String, String> storedValueMap = valueMapOpt.get();
        assertEquals(2, storedValueMap.get("foo").size());
        assertEquals("bar1", storedValueMap.get("foo").get(0));
        assertEquals("bar2", storedValueMap.get("foo").get(1));
        assertEquals(1, storedValueMap.get("baz").size());
        assertEquals("qux&key=value", storedValueMap.getFirst("baz"));
        assertEquals("=== /test!", storedValueMap.getFirst("&#a"));
    }
}
