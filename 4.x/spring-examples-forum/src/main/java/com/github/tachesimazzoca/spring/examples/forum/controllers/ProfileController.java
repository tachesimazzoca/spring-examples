package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.ProfileEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.ProfileEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.User;
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
@RequestMapping(value = "/profile")
public class ProfileController extends AbstractUserController {
    private static final Logger LOGGER = Logger.getLogger(ProfileController.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private ProfileEditFormValidator profileEditFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @InitBinder("profileEditForm")
    public void initProfileEditFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(ProfileEditForm.getAllowedFields());
        binder.setValidator(profileEditFormValidator);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@ModelAttribute("user") User user,
                       @ModelAttribute("profileEditForm") ProfileEditForm form) {
        Account account = user.getAccount();
        form.setId(String.valueOf(account.getId()));
        form.setEmail(account.getEmail());
        form.setNickname(account.getNickname());
        return "profile/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@ModelAttribute("user") User user,
                           @Validated @ModelAttribute("profileEditForm") ProfileEditForm form,
                           BindingResult errors) {
        Account account = user.getAccount();
        if (!account.getId().equals(Long.parseLong(form.getId()))) {
            // The current user isn't the owner of this account.
            return "redirect:/dashboard";
        }

        if (!errors.hasFieldErrors("email")) {
            if (!form.getEmail().equals(account.getEmail())) {
                if (accountDao.findByEmail(form.getEmail()).isPresent()) {
                    errors.rejectValue("email", "unique.email");
                }
            }
        }

        if (!form.getPassword().isEmpty()) {
            if (!account.isValidPassword(form.getCurrentPassword())) {
                errors.rejectValue("currentPassword", "equalTo.currentPassword");
            }
        }

        if (errors.hasErrors()) {
            return "profile/edit";
        }

        account.setNickname(form.getNickname());
        if (!form.getPassword().isEmpty()) {
            account.refreshPassword(form.getPassword());
        }
        accountDao.save(account);

        if (account.getEmail().equals(form.getEmail())) {
            return "redirect:/dashboard";
        }

        // Store the parameters temporarily
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add("id", String.valueOf(account.getId()));
        valueMap.add("email", form.getEmail());
        String code = verificationStorage.create(valueMap);
        String url = UriComponentsBuilder.fromUriString((String) config.get("url.http"))
                .path(config.get("url.basedir") + "/verification/profile")
                .queryParam("code", code)
                .build().toUriString();
        LOGGER.info(url);

        return "/profile/verify";
    }
}
