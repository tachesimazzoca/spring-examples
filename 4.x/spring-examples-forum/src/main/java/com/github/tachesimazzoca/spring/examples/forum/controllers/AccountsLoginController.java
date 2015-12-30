package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountsLoginForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountsLoginFormValidator;
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

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/accounts")
public class AccountsLoginController extends AbstractUserController {
    @Autowired
    private Timer timer;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountsLoginFormValidator accountsLoginFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @InitBinder("accountsLoginForm")
    public void initAccountsEntryFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(AccountsLoginForm.getAllowedFields());
        binder.setValidator(accountsLoginFormValidator);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@ModelAttribute AccountsLoginForm form,
                        HttpSession session) {
        session.removeAttribute(UserSession.KEY);
        return "accounts/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String postLogin(@Validated @ModelAttribute AccountsLoginForm form,
                            BindingResult errors,
                            HttpSession session) {
        if (errors.hasErrors()) {
            return "accounts/login";
        }

        Account account = accountDao.findByEmail(form.getEmail()).orElse(null);
        if (null == account || !account.getStatus().equals(Account.Status.ACTIVE)
                || !account.isValidPassword(form.getPassword())) {
            errors.reject("AssertTrue.authorized");
            return "accounts/login";
        }

        UserSession userSession = new UserSession();
        userSession.setLastAccessedTime(timer.currentTimeMillis());
        userSession.setAccountId(account.getId());
        session.setAttribute(UserSession.KEY, userSession);

        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout() {
        return "redirect:/accounts/login";
    }
}
