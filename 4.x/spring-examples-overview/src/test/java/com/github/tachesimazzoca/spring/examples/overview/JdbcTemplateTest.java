package com.github.tachesimazzoca.spring.examples.overview;

import com.github.tachesimazzoca.spring.examples.overview.models.Account;
import com.github.tachesimazzoca.spring.examples.overview.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.overview.models.JdbcAccountDao;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JdbcTemplateTest {
    private static final ApplicationContext context = new ClassPathXmlApplicationContext(
            "spring/database.xml");

    private DataSource dataSource() {
        return context.getBean("dataSource", DataSource.class);
    }

    private void resetTables(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("TRUNCATE TABLE accounts");
        jdbcTemplate.update("ALTER TABLE accounts ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    public void testGeneratedKeyHolder() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        resetTables(jdbcTemplate);

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement pstmt = conn.prepareStatement(
                                "INSERT INTO accounts (email, status) VALUES (?, ?)", new String[]{"id"});
                        pstmt.setString(1, "user1@example.net");
                        pstmt.setInt(2, 0);
                        return pstmt;
                    }
                }, keyHolder);
        assertEquals(1L, keyHolder.getKey());
    }

    @Test
    public void testSimpleJdbcInsert() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        resetTables(jdbcTemplate);

        SimpleJdbcInsert updater = new SimpleJdbcInsert(dataSource());
        updater.setTableName("accounts");
        updater.usingColumns("email", "status");
        updater.usingGeneratedKeyColumns("id");
        MapSqlParameterSource bind = new MapSqlParameterSource();
        bind.addValue("email", "user1@example.net");
        bind.addValue("status", 0);
        Number id = updater.executeAndReturnKey(bind);
        assertEquals(1L, id);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM accounts");
        assertEquals(1, rows.size());
        Map<String, Object> row = rows.get(0);
        assertEquals(id, row.get("id"));
        assertEquals("user1@example.net", row.get("email"));
        assertEquals((byte) 0, row.get("status"));
    }

    @Test
    public void testQueryForObject() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        resetTables(jdbcTemplate);

        jdbcTemplate.update("INSERT INTO accounts (id, email, status) VALUES (1, 'user1@example.net', 0)");
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM accounts", Integer.class);
        assertEquals(1, count);
    }

    @Test
    public void testRowMapper() {
        DataSource ds = dataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        resetTables(jdbcTemplate);

        AccountDao dao = new JdbcAccountDao(ds);
        assertNull(dao.find(123L));

        jdbcTemplate.update("INSERT INTO accounts (id, email, status) VALUES (1, 'user1@example.net', 1)");
        Account expected = new Account(1L, "user1@example.net", Account.Status.ACTIVE);
        assertEquals(expected, dao.find(1L));
    }
}
