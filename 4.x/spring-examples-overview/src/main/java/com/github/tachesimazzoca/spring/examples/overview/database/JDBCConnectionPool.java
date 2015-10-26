package com.github.tachesimazzoca.spring.examples.overview.database;

import java.sql.Connection;

public interface JDBCConnectionPool {
    public Connection getConnection();

    public String getDriver();

    public String getUrl();

    public String getUser();

    public String getPassword();
}
