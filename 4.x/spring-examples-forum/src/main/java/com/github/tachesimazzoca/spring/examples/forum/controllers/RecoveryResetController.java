package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.RecoveryResetForm;
import com.github.tachesimazzoca.spring.examples.forum.models.RecoveryResetFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.storage.MultiValueMapStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/recovery")
public class RecoveryResetController extends AbstractUserController {
    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RecoveryResetFormValidator recoveryResetFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @InitBinder("recoveryResetForm")
    public void initRecoveryResetFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(RecoveryResetForm.getAllowedFields());
        binder.setValidator(recoveryResetFormValidator);
    }

    @ModelAttribute("recoveryResetForm")
    public RecoveryResetForm recoveryResetForm(@RequestParam("code") String code) {
        if (!verificationStorage.read(code).isPresent())
            throw new NoSuchContentException("/recovery/errors/session");
        RecoveryResetForm form = new RecoveryResetForm();
        form.setCode(code);
        return form;
    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public String reset(@ModelAttribute RecoveryResetForm form) {
        return "recovery/reset";
    }

    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String postEntry(@Validated @ModelAttribute RecoveryResetForm form,
                            BindingResult errors) {
        if (errors.hasErrors()) {
            return "recovery/reset";
        }

        MultiValueMap<String, String> valueMap = verificationStorage.read(
                form.getCode()).orElse(null);
        if (null == valueMap)
            throw new NoSuchContentException("/recovery/errors/session");
        verificationStorage.delete(form.getCode());

        Long id = Long.valueOf(valueMap.toSingleValueMap().get("id"));
        Account account = accountDao.find(id).orElse(null);
        if (null == account || !account.isActive())
            throw new NoSuchContentException("/recovery/errors/session");

        account.refreshPassword(form.getPassword());
        accountDao.save(account);

        return "recovery/complete";
    }
}
