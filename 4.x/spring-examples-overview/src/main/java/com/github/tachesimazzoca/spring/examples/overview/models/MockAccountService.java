package com.github.tachesimazzoca.spring.examples.overview.models;

public class MockAccountService implements AccountService {
    private final AccountDao accountDao;

    public MockAccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account getAccountById(Long id) {
        return accountDao.find(id);
    }
}
