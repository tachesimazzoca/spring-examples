package com.github.tachesimazzoca.spring.examples.overview.database;

import java.sql.Connection;

public class MockJDBCConnectionPool implements JDBCConnectionPool {
    private final String driver;
    private final String url;
    private final String user;
    private final String password;

    public MockJDBCConnectionPool(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Connection getConnection() {
        return null;
    }
}
