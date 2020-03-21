package com.github.tachesimazzoca.spring.overview.service;

import com.github.tachesimazzoca.spring.overview.dao.AccountDaoImpl;
import com.github.tachesimazzoca.spring.overview.dao.MockAccountDaoImpl;

public class ServiceLocator {
    private Profile profile = Profile.PRODUCTION;

    public ServiceLocator(Profile profile) {
        this.profile = profile;
    }

    public AccountService createAccountService() {
        AccountService obj;
        if (this.profile == Profile.DEVELOPMENT) {
            obj = new MockAccountServiceImpl();
        } else {
            obj = new AccountServiceImpl();
            obj.setAccountDao((this.profile == Profile.TEST)
                ? new MockAccountDaoImpl() : new AccountDaoImpl());
        }
        return obj;
    }

    public enum Profile {
        PRODUCTION,
        TEST,
        DEVELOPMENT
    }
}
