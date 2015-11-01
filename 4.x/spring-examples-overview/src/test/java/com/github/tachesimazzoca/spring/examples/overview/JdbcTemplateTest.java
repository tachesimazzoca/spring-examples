package com.github.tachesimazzoca.spring.examples.overview;

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
    private DataSource dataSource() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/database.xml");
        return ctx.getBean("dataSource", DataSource.class);
    }

    private void createTables(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS accounts");
        jdbcTemplate.execute("CREATE TABLE `accounts` ("
                + "`id` BIGINT NOT NULL AUTO_INCREMENT,"
                + "`email` VARCHAR(255) NOT NULL default '' UNIQUE,"
                + "`status` TINYINT(1) NOT NULL default 0,"
                + " PRIMARY KEY (`id`))");
    }

    private void dropTables(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS accounts");
    }

    @Test
    public void testGeneratedKeyHolder() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        createTables(jdbcTemplate);

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

        dropTables(jdbcTemplate);
    }

    @Test
    public void testSimpleJdbcInsert() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        createTables(jdbcTemplate);

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

        dropTables(jdbcTemplate);
    }
}
