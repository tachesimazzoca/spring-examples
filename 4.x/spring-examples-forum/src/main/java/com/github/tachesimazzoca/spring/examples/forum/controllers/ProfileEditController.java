package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import com.github.tachesimazzoca.spring.examples.forum.helpers.FileHelper;
import com.github.tachesimazzoca.spring.examples.forum.helpers.TempFileHelper;
import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.ProfileEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.ProfileEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.User;
import com.github.tachesimazzoca.spring.examples.forum.storage.MultiValueMapStorage;
import com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = "/profile")
public class ProfileEditController {
    private static final Logger LOGGER = Logger.getLogger(ProfileEditController.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ProfileEditFormValidator profileEditFormValidator;

    @Autowired
    @Qualifier("verificationStorage")
    private MultiValueMapStorage verificationStorage;

    @Autowired
    @Qualifier("profileIconHelper")
    private FileHelper profileIconHelper;

    @Autowired
    private TempFileHelper tempFileHelper;

    @InitBinder("profileEditForm")
    public void initProfileEditFormBinder(WebDataBinder binder) {
        binder.setAllowedFields(ProfileEditForm.getAllowedFields());
        binder.setValidator(profileEditFormValidator);
    }

    @ModelAttribute
    public ProfileEditForm createProfileEditForm(@ModelAttribute User user) {
        Account account = user.getAccount();
        if (null == account)
            throw new UserSessionException("/profile/edit");
        ProfileEditForm form = new ProfileEditForm();
        form.setEmail(account.getEmail());
        form.setNickname(account.getNickname());
        form.setCurrentAccount(account);
        return form;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(@ModelAttribute User user,
                       @RequestParam(value = "flash", defaultValue = "0") boolean flash,
                       Model model) {
        model.addAttribute("flash", flash);
        model.addAttribute("icon", profileIconHelper.find(
                String.valueOf(user.getAccount().getId())).isPresent());
        return "profile/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String postEdit(@ModelAttribute User user,
                           @Validated @ModelAttribute ProfileEditForm form,
                           BindingResult errors,
                           Model model) {

        model.addAttribute("icon", profileIconHelper.find(
                String.valueOf(user.getAccount().getId())).isPresent());

        if (errors.hasErrors()) {
            return "profile/edit";
        }

        Account account = user.getAccount();
        account.setNickname(form.getNickname());
        if (!form.getPassword().isEmpty()) {
            account.refreshPassword(form.getPassword());
        }
        accountDao.save(account);

        String iconToken = ParameterUtils.nullTo(form.getIconToken(), "");
        if (!iconToken.isEmpty()) {
            File tempFile = tempFileHelper.read(iconToken).orElse(null);
            if (null != tempFile) {
                String extension = FilenameUtils.getExtension(tempFile.getName());
                String iconName = String.valueOf(account.getId());
                try {
                    profileIconHelper.delete(iconName);
                    profileIconHelper.save(FileUtils.openInputStream(tempFile),
                            iconName, extension);
                } catch (IOException e) {
                    // Fail gracefully on IOException
                } finally {
                    FileUtils.deleteQuietly(tempFile);
                }
            }
        }

        if (account.getEmail().equals(form.getEmail())) {
            return "redirect:/profile/edit?flash=1";
        }

        // Store the parameters temporarily
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.add("id", String.valueOf(account.getId()));
        valueMap.add("email", form.getEmail());
        String code = verificationStorage.create(valueMap);
        String url = UriComponentsBuilder.fromUriString((String) config.get("url.http"))
                .path(config.get("url.basedir") + "/profile/activate")
                .queryParam("code", code)
                .build().toUriString();
        LOGGER.info(url);

        return "/profile/verify";
    }
}
