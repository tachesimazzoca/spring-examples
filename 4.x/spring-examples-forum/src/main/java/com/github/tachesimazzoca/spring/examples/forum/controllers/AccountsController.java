package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountsEntryForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountsEntryFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountsLoginForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountsLoginFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.util.Timer;
import com.github.tachesimazzoca.spring.examples.forum.sessions.UserSession;
import com.github.tachesimazzoca.spring.examples.forum.storage.MultiValueMapStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/accounts")
public class AccountsController {
    private static final Logger LOGGER = Logger.getLogger(AccountsController.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private Timer timer;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountsEntryFormValidator accountsEntryFormValidator;

    @Autowired
    private AccountsLoginFormValidator accountsLoginFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @InitBinder("accountsEntryForm")
    public void initAccountsEntryFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(AccountsEntryForm.getAllowedFields());
        binder.setValidator(accountsEntryFormValidator);
    }

    @InitBinder("accountsLoginForm")
    public void initAccountsLoginFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(AccountsLoginForm.getAllowedFields());
        binder.setValidator(accountsLoginFormValidator);
    }

    @RequestMapping(value = "/errors/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String errors(@PathVariable("name") String name) {
        return "accounts/errors/" + name;
    }

    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public String entry(@ModelAttribute("accountsEntryForm") AccountsEntryForm form) {
        return "accounts/entry";
    }

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    public String postEntry(@Validated @ModelAttribute("accountsEntryForm") AccountsEntryForm form,
                            BindingResult errors) {
        if (errors.hasErrors()) {
            return "accounts/entry";
        }

        // Store the parameters temporarily
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
        valueMap.add("email", form.getEmail());
        valueMap.add("password", form.getPassword());
        String code = verificationStorage.create(valueMap);
        String url = UriComponentsBuilder.fromUriString((String) config.get("url.http"))
                .path(config.get("url.basedir") + "/accounts/activate")
                .queryParam("code", code)
                .build().toUriString();
        LOGGER.info(url);

        return "accounts/verify";
    }

    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public String activate(@RequestParam("code") String code, Model model) {
        Optional<MultiValueMap<String, String>> valueMapOpt = verificationStorage.read(code);
        if (!valueMapOpt.isPresent()) {
            return "redirect:/accounts/errors/session";
        }

        Map<String, String> params = valueMapOpt.get().toSingleValueMap();
        if (accountDao.findByEmail(params.get("email")).isPresent()) {
            // The email has been registered
            return "redirect:/accounts/errors/email";
        }

        Account account = new Account();
        account.setEmail(params.get("email"));
        account.setNickname("");
        account.setStatus(Account.Status.ACTIVE);
        account.refreshPassword(params.get("password"));
        Account savedAccount = accountDao.save(account);
        model.addAttribute("account", savedAccount);

        return "accounts/activate";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(@ModelAttribute("accountsLoginForm") AccountsLoginForm form,
                        HttpSession session) {
        session.removeAttribute(UserSession.KEY);
        return "accounts/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String postLogin(@Validated @ModelAttribute("accountsLoginForm") AccountsLoginForm form,
                            BindingResult errors,
                            HttpSession session) {
        session.removeAttribute(UserSession.KEY);

        if (errors.hasErrors()) {
            return "accounts/login";
        }
        Account account = accountDao.findByEmail(form.getEmail()).orElse(null);
        if (null == account || !account.getStatus().equals(Account.Status.ACTIVE)
                || !account.isValidPassword(form.getPassword())) {
            form.setDenied(true);
            return "accounts/login";
        }

        UserSession userSession = new UserSession();
        userSession.setLastAccessedTime(timer.currentTimeMillis());
        userSession.setAccountId(account.getId());
        session.setAttribute(UserSession.KEY, userSession);

        return "redirect:/dashboard";
    }
}
