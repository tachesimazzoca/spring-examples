package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.config.Config;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.Storage;
import com.github.tachesimazzoca.spring.examples.forum.views.AccountsEntryForm;
import com.github.tachesimazzoca.spring.examples.forum.views.helpers.FormHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils.*;

@Controller
@RequestMapping(value = "/accounts")
public class AccountsController {
    private static final Logger LOGGER = Logger.getLogger(AccountsController.class.getName());

    @Autowired
    private Config config;

    @Autowired
    private ValidatorFactory validatorFactory;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    @Qualifier("verificationStorage")
    private Storage verificationStorage;

    @RequestMapping(value = "/errors/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String errors(@PathVariable("name") String name) {
        return "accounts/errors/" + name;
    }

    @RequestMapping(value = "/entry", method = RequestMethod.GET)
    public ModelAndView entry() {
        ModelAndView view = new ModelAndView("accounts/entry");
        view.addObject("form", new FormHelper(AccountsEntryForm.defaultForm()));
        return view;
    }

    @RequestMapping(value = "/entry", method = RequestMethod.POST)
    public String postEntry(@RequestBody MultiValueMap<String, String> formData)
            throws ValidatorException {

        AccountsEntryForm form = AccountsEntryForm.bindFrom(formData);
        Validator validator = validatorFactory.getValidator();

        // Check if the email has been registered
        if (validator.validateProperty(form, "email").isEmpty()) {
            if (!form.getEmail().isEmpty()) {
                if (accountDao.findByEmail(form.getEmail()).isPresent()) {
                    form.setUniqueEmail(false);
                }
            }
        }

        Set<ConstraintViolation<AccountsEntryForm>> errors = validator.validate(form);
        if (!errors.isEmpty()) {
            ModelAndView view = new ModelAndView("accounts/entry");
            view.addObject("form", new FormHelper(form, errors));
            throw new ValidatorException(view);
        }

        // Store the parameters temporally
        Map<String, Object> params = params(
                "email", form.getEmail(),
                "password", form.getPassword());
        String code = verificationStorage.create(params);
        String url = UriComponentsBuilder.fromUriString((String) config.get("url.http"))
                .path(config.get("url.basedir") + "/accounts/activate")
                .queryParam("code", code)
                .build().toUriString();
        LOGGER.info(url);

        return "accounts/verify";
    }

    public class ValidatorException extends Exception {
        private final ModelAndView view;

        public ValidatorException(ModelAndView view) {
            this.view = view;
        }

        public ModelAndView getView() {
            return view;
        }
    }

    @ExceptionHandler(AccountsController.ValidatorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handle(ValidatorException e) {
        return e.getView();
    }
}
