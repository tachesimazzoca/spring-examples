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
@RequestMapping(value = "/profile")
public class ProfileController extends AbstractUserController {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public String profile(@RequestParam("code") String code, Model model) {
        MultiValueMap<String, String> valueMap = verificationStorage.read(code).orElse(null);
        if (null == valueMap)
            throw new NoSuchContentException("/errors/session");
        verificationStorage.delete(code);

        Map<String, String> params = valueMap.toSingleValueMap();
        if (accountDao.findByEmail(params.get("email")).isPresent())
            throw new NoSuchContentException("/errors/session");

        Account account = accountDao.find(Long.parseLong(params.get("id"))).orElse(null);
        if (null == account)
            throw new NoSuchContentException("/errors/session");

        account.setEmail(params.get("email"));
        Account savedAccount = accountDao.save(account);
        model.addAttribute("account", savedAccount);

        return "profile/activate";
    }
}
