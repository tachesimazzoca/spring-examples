package com.github.tachesimazzoca.spring.overview.service;

import com.github.tachesimazzoca.spring.overview.dao.AccountDaoImpl;

public final class AccountServiceFactory {
    private AccountServiceFactory() {
    }

    public static AccountService create(String name) {
        Class<?> clazz;
        AccountService obj;
        try {
            clazz = Class.forName(
                    "com.github.tachesimazzoca.spring.overview.service."
                    + name + "AccountServiceImpl");
            obj = (AccountService) clazz.newInstance();
            if (name.isEmpty()) {
                obj.setAccountDao(new AccountDaoImpl());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}
