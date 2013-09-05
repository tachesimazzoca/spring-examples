package com.github.tachesimazzoca.spring.overview;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.tachesimazzoca.spring.overview.service.AccountService;
import com.github.tachesimazzoca.spring.overview.entity.Account;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] {"spring/application.xml"});

        List<String> serviceKeys = Arrays.asList(
            "AccountService",
            "MockAccountService",
            "DevAccountService",
            "TestAccountService"
        );
        for (String key : serviceKeys) {
            AccountService service = context.getBean(key, AccountService.class);
            System.out.println(service);
            System.out.println(Arrays.toString(service.getAllAccountNames()));
        }

        List<String> accountKeys = Arrays.asList(
            "SingletonAccount",
            "PrototypeAccount"
        );
        for (int i = 0; i < 2; i++) {
            for (String key : accountKeys) {
                Account account = context.getBean(key, Account.class);
                System.out.println(account);
                System.out.println(account.getName());
                account.setName("name-" + i);
            }
        }
    }
}
