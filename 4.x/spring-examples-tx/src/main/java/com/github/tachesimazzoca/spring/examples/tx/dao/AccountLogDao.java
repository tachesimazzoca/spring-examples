package com.github.tachesimazzoca.spring.examples.tx.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class AccountLogDao {
    private DataSource dataSource;

    @Autowired
    public AccountLogDao(@Qualifier("subDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(java.sql.Date accessDate, String message) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            stmt = conn.prepareStatement(
                    "INSERT INTO account_log (access_date, message) VALUES (?, ?)");
            stmt.setDate(1, accessDate);
            stmt.setString(2, message);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }
}
