package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.RecoveryEntryForm;
import com.github.tachesimazzoca.spring.examples.forum.models.RecoveryEntryFormValidator;
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
@RequestMapping(value = "/recovery")
public class RecoveryEntryController {
    private static final Logger LOGGER = Logger.getLogger(RecoveryEntryController.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RecoveryEntryFormValidator recoveryEntryFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @InitBinder("recoveryEntryForm")
    public void initRecoveryEntryFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(RecoveryEntryForm.getAllowedFields());
        binder.setValidator(recoveryEntryFormValidator);
    }

    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public String entry(@ModelAttribute RecoveryEntryForm form) {
        return "recovery/entry";
    }

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    public String postEntry(@Validated @ModelAttribute RecoveryEntryForm form,
                            BindingResult errors) {
        if (errors.hasErrors()) {
            return "recovery/entry";
        }

        Account account = accountDao.findByEmail(form.getEmail()).orElse(null);
        if (null == account || !account.isActive()) {
            throw new NoSuchContentException("/recovery/entry");
        }

        // Store the parameters temporarily
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<String, String>();
        valueMap.add("id", String.valueOf(account.getId()));
        String code = verificationStorage.create(valueMap);
        String url = UriComponentsBuilder.fromUriString((String) config.get("url.http"))
                .path(config.get("url.basedir") + "/recovery/reset")
                .queryParam("code", code)
                .build().toUriString();
        LOGGER.info(url);

        return "recovery/verify";
    }
}
