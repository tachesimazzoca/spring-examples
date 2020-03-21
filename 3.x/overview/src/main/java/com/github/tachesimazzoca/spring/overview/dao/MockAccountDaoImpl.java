package com.github.tachesimazzoca.spring.overview.dao;

public class MockAccountDaoImpl implements AccountDao {
    public String[] getAllAccountNames() {
        String[] names = {"foo", "bar", "baz"};
        return names;
    }
}
