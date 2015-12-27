package com.github.tachesimazzoca.spring.examples.forum.models;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProfileEditFormValidator implements Validator {

    private final EmailValidator emailRule = EmailValidator.getInstance(false);
    private final RegexValidator passwordRule = new RegexValidator("^.{8,}$", false);

    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileEditForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProfileEditForm form = (ProfileEditForm) o;

        // email
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "notEmpty.email");
        if (!errors.hasFieldErrors("email")) {
            if (!emailRule.isValid(form.getEmail())) {
                errors.rejectValue("email", "pattern.email");
            }
        }

        // password
        if (!form.getPassword().isEmpty()) {
            if (!errors.hasFieldErrors("password")) {
                if (!passwordRule.isValid(form.getPassword())) {
                    errors.rejectValue("password", "pattern.password");
                }
            }
            if (!form.isValidRetypedPassword()) {
                errors.rejectValue("retypedPassword", "equalTo.retypedPassword");
            }
        }
    }
}
