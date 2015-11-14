package com.github.tachesimazzoca.spring.examples.forum.controllers;

import com.github.tachesimazzoca.spring.examples.forum.views.AccountsEntryForm;
import com.github.tachesimazzoca.spring.examples.forum.views.helpers.FormHelper;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Controller
@RequestMapping(value = "/accounts")
public class AccountsController {
    @Autowired
    private Validator validator;

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
    public String postEntry(@RequestBody MultiValueMap<String, String> params)
            throws ValidatorException {
        AccountsEntryForm form = AccountsEntryForm.bindFrom(params);
        Set<ConstraintViolation<AccountsEntryForm>> errors = validator.validate(form);
        if (!errors.isEmpty()) {
            ModelAndView view = new ModelAndView("accounts/entry");
            view.addObject("form", new FormHelper(form, errors));
            throw new ValidatorException(view);
        }
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
