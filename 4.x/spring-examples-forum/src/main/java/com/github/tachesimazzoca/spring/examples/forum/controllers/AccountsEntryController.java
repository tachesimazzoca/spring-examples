package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountsEntryForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountsEntryFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.storage.MultiValueMapStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/accounts")
public class AccountsEntryController extends AbstractUserController {
    private static final Logger LOGGER = Logger.getLogger(AccountsEntryController.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountsEntryFormValidator accountsEntryFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @InitBinder("accountsEntryForm")
    public void initAccountsEntryFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(AccountsEntryForm.getAllowedFields());
        binder.setValidator(accountsEntryFormValidator);
    }

    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public String entry(@ModelAttribute AccountsEntryForm form) {
        return "accounts/entry";
    }

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    public String postEntry(@Validated @ModelAttribute AccountsEntryForm form,
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
                .path(config.get("url.basedir") + "/verification/account")
                .queryParam("code", code)
                .build().toUriString();
        LOGGER.info(url);

        return "accounts/verify";
    }
}
