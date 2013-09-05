package com.github.tachesimazzoca.spring.overview.service;

import com.github.tachesimazzoca.spring.overview.dao.AccountDao;

public interface AccountService {
    void setAccountDao(AccountDao accountDao);

    String[] getAllAccountNames();
}
