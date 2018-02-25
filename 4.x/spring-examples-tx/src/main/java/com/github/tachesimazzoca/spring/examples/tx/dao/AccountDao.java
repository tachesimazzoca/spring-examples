package com.github.tachesimazzoca.spring.examples.tx.dao;

import com.github.tachesimazzoca.spring.examples.tx.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.NoSuchElementException;

@Component
public class AccountDao {
    private DataSource dataSource;

    @Autowired
    public AccountDao(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // Without the transaction management,
    // any connections must be closed manually.

    public Account findWithoutTx(Long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            stmt.setLong(1, id);
            rset = stmt.executeQuery();
            if (rset.next()) {
                Account account = new Account(
                        rset.getLong("id"),
                        rset.getString("username"),
                        new java.util.Date(rset.getTimestamp("modified_at").getTime()));
                return account;
            } else {
                throw new NoSuchElementException();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rset);
            JdbcUtils.closeStatement(stmt);
            JdbcUtils.closeConnection(conn);
        }
    }

    // For working with the transaction management properly,
    // any connections must be get and released via. DataSourceUtils(JDBC)

    public Account find(Long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            stmt = conn.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            stmt.setLong(1, id);
            rset = stmt.executeQuery();
            if (rset.next()) {
                Account account = new Account(
                        rset.getLong("id"),
                        rset.getString("username"),
                        new java.util.Date(rset.getTimestamp("modified_at").getTime()));
                return account;
            } else {
                throw new NoSuchElementException();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeResultSet(rset);
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    public void create(Account account) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            stmt = conn.prepareStatement("INSERT INTO accounts" +
                    " (id, username, modified_at) VALUES (?, ?, ?)");
            stmt.setLong(1, account.id);
            stmt.setString(2, account.username);
            stmt.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeStatement(stmt);
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }
}
