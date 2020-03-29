package com.github.tachesimazzoca.spring.examples.forum.interfaces.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.github.tachesimazzoca.spring.examples.forum.application.AccountService;

@RestController
@RequestMapping(value = "/account")
public class AccountEntryController {

    private static final Logger logger = LoggerFactory.getLogger(AccountEntryController.class);

    @Autowired
    AccountEntryFormValidator accountEntryFormValidator;

    @Autowired
    AccountService accountService;

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    public AccountEntryResponse postEntry(@RequestBody AccountEntryForm form,
            BindingResult errors) {

        accountEntryFormValidator.validate(form, errors);
        if (!errors.hasErrors()) {
            accountService.attempt(form.getEmail(), form.getPassword());
        }
        AccountEntryResponse response = new AccountEntryResponse();
        response.setAccountEntryForm(form);
        response.setErrors(errors.getAllErrors());
        logger.info(errors.toString());

        return response;
    }
}
