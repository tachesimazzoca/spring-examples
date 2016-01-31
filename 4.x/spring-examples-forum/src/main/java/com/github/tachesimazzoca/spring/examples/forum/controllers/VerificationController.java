package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.storage.MultiValueMapStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/verification")
public class VerificationController extends AbstractUserController {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @RequestMapping(
            value = "/errors/{reason:(?:session|email)}",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String errors(@PathVariable("reason") String reason) {
        return "verification/errors/" + reason;
    }

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String account(@RequestParam("code") String code, Model model) {
        Optional<MultiValueMap<String, String>> valueMapOpt = verificationStorage.read(code);
        if (!valueMapOpt.isPresent())
            throw new NoSuchContentException("/verification/errors/session");
        verificationStorage.delete(code);

        Map<String, String> params = valueMapOpt.get().toSingleValueMap();
        if (accountDao.findByEmail(params.get("email")).isPresent())
            throw new NoSuchContentException("/verification/errors/email");

        Account account = new Account();
        account.setEmail(params.get("email"));
        account.setNickname("");
        account.setStatus(Account.Status.ACTIVE);
        account.refreshPassword(params.get("password"));
        Account savedAccount = accountDao.save(account);
        model.addAttribute("account", savedAccount);

        return "verification/account";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(@RequestParam("code") String code, Model model) {
        Optional<MultiValueMap<String, String>> valueMapOpt = verificationStorage.read(code);
        if (!valueMapOpt.isPresent())
            throw new NoSuchContentException("/verification/errors/session");
        verificationStorage.delete(code);

        Map<String, String> params = valueMapOpt.get().toSingleValueMap();
        if (accountDao.findByEmail(params.get("email")).isPresent())
            throw new NoSuchContentException("/verification/errors/email");

        Account account = accountDao.find(Long.parseLong(params.get("id"))).orElse(null);
        if (null == account)
            throw new NoSuchContentException("/verification/errors/session");

        account.setEmail(params.get("email"));
        Account savedAccount = accountDao.save(account);
        model.addAttribute("account", savedAccount);

        return "verification/profile";
    }
}
