package com.github.tachesimazzoca.spring.examples.overview.database;

import java.sql.Connection;

public class MockJDBCConnectionPool implements JDBCConnectionPool {
    private final String driverClassName;
    private final String url;
    private final String username;
    private final String password;

    public MockJDBCConnectionPool(String driverClassName, String url, String username, String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    @Override
    public String getDriverClassName() {
        return driverClassName;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
