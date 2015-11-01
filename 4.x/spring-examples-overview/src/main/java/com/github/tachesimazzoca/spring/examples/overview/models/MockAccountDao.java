package com.github.tachesimazzoca.spring.examples.overview.models;

public class MockAccountDao implements AccountDao {
    @Override
    public Account find(Long id) {
        return new Account(id, "", Account.Status.ACTIVE);
    }
}
