package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountEntryForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountEntryFormValidator;
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
@RequestMapping(value = "/account")
public class AccountEntryController {
    private static final Logger LOGGER = Logger.getLogger(AccountEntryController.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private AccountEntryFormValidator accountEntryFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @InitBinder("accountEntryForm")
    public void initAccountEntryFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(AccountEntryForm.getAllowedFields());
        binder.setValidator(accountEntryFormValidator);
    }

    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public String entry(@ModelAttribute AccountEntryForm form) {
        return "account/entry";
    }

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    public String postEntry(@Validated @ModelAttribute AccountEntryForm form,
                            BindingResult errors) {
        if (errors.hasErrors()) {
            return "account/entry";
        }

        // Store the parameters temporarily
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
        valueMap.add("email", form.getEmail());
        valueMap.add("password", form.getPassword());
        String code = verificationStorage.create(valueMap);
        String url = UriComponentsBuilder.fromUriString((String) config.get("url.http"))
                .path(config.get("url.basedir") + "/account/activate")
                .queryParam("code", code)
                .build().toUriString();
        LOGGER.info(url);

        return "account/verify";
    }
}
