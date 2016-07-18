package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.storage.MultiValueMapStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/account")
public class AccountController {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public String activate(@RequestParam("code") String code, Model model) {
        MultiValueMap<String, String> valueMap = verificationStorage.read(code).orElse(null);
        if (null == valueMap)
            throw new NoSuchContentException("/error/session");
        verificationStorage.delete(code);

        Map<String, String> params = valueMap.toSingleValueMap();
        if (accountDao.findByEmail(params.get("email")).isPresent())
            throw new NoSuchContentException("/error/session");

        Account account = new Account();
        account.setEmail(params.get("email"));
        account.setNickname("");
        account.setStatus(Account.Status.ACTIVE);
        account.refreshPassword(params.get("password"));
        Account savedAccount = accountDao.save(account);
        model.addAttribute("account", savedAccount);

        return "account/activate";
    }
}
