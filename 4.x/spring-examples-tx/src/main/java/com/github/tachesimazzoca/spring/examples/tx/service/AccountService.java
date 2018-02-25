package com.github.tachesimazzoca.spring.examples.tx.service;

import com.github.tachesimazzoca.spring.examples.tx.dao.AccountLogDao;
import com.github.tachesimazzoca.spring.examples.tx.dao.AccountDao;
import com.github.tachesimazzoca.spring.examples.tx.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccountService {
    private AccountDao accountDao;
    private AccountLogDao accountLogDao;

    @Autowired
    public AccountService(AccountDao accountDao, AccountLogDao accountLogDao) {
        this.accountDao = accountDao;
        this.accountLogDao = accountLogDao;
    }

    public Account getAccount(Long id) {
        return accountDao.find(id);
    }

    @Transactional("txManager")
    public Account[] createAccounts(Account[] accounts) {
        Account[] savedAccounts = new Account[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            accountDao.create(accounts[i]);
            //============================
            Account savedAccount = accountDao.find(accounts[i].id);
            //-------------------
            // Using the following method will cause NoSuchElementException
            // because the connection isolated from txManager can never find the created one.
            //Account savedAccount = accountDao.findWithoutTx(accounts[i].id);
            //============================
            savedAccounts[i] = savedAccount;
        }
        return savedAccounts;
    }

    @Transactional("subTxManager")
    public void writeAccountLog(String message) {
        accountLogDao.insert(new java.sql.Date(System.currentTimeMillis()), message);
    }
}
