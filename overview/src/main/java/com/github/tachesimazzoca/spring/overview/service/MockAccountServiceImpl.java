package com.github.tachesimazzoca.spring.overview.service;

import com.github.tachesimazzoca.spring.overview.dao.AccountDao;

public class MockAccountServiceImpl implements AccountService {
    public void setAccountDao(AccountDao accountDao) {
    }

    public String[] getAllAccountNames() {
        String[] names = {"fuga", "qux", "qqux"};
        return names;
    }
}
