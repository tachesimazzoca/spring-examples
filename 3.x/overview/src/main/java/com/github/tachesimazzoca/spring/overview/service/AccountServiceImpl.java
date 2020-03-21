package com.github.tachesimazzoca.spring.overview.service;

import com.github.tachesimazzoca.spring.overview.dao.AccountDao;

public class AccountServiceImpl implements AccountService {
    private AccountDao accountDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public String[] getAllAccountNames() {
        return this.accountDao.getAllAccountNames();
    }
}
