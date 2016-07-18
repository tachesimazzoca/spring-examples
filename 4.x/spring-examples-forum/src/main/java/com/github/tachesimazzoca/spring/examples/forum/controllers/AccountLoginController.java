package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountLoginForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountLoginFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.sessions.UserSession;
import com.github.tachesimazzoca.spring.examples.forum.storage.MultiValueMapStorage;
import com.github.tachesimazzoca.spring.examples.forum.util.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/account")
public class AccountLoginController {
    @Autowired
    private Timer timer;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountLoginFormValidator accountLoginFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @InitBinder("accountLoginForm")
    public void initAccountLoginFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(AccountLoginForm.getAllowedFields());
        binder.setValidator(accountLoginFormValidator);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@ModelAttribute AccountLoginForm form,
                        @RequestParam(name = "returnTo", required = false) String returnTo) {

        if (null != returnTo && returnTo.startsWith("/"))
            form.setReturnTo(returnTo);

        return "account/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String postLogin(@Validated @ModelAttribute AccountLoginForm form,
                            BindingResult errors,
                            HttpSession session) {
        if (errors.hasErrors()) {
            return "account/login";
        }

        Account account = accountDao.findByEmail(form.getEmail()).orElse(null);
        if (null == account || !account.getStatus().equals(Account.Status.ACTIVE)
                || !account.isValidPassword(form.getPassword())) {
            errors.reject("AssertTrue.authorized", "Authentication failed.");
            return "account/login";
        }

        UserSession userSession = new UserSession();
        userSession.setLastAccessedTime(timer.currentTimeMillis());
        userSession.setAccountId(account.getId());
        session.setAttribute(UserSession.KEY, userSession);

        String returnTo = form.getReturnTo();
        if (null == returnTo || !returnTo.startsWith("/")) {
            returnTo = "/dashboard";
        }

        return "redirect:" + returnTo;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "redirect:/account/login";
    }
}
