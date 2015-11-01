package com.github.tachesimazzoca.spring.examples.overview.database;

import java.sql.Connection;

public interface JDBCConnectionPool {
    public Connection getConnection();

    public String getDriverClassName();

    public String getUrl();

    public String getUsername();

    public String getPassword();
}
