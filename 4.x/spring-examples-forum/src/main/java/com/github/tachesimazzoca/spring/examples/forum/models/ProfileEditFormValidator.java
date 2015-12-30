package com.github.tachesimazzoca.spring.examples.forum.models;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProfileEditFormValidator implements Validator {
    @Autowired
    private AccountDao accountDao;

    private final EmailValidator emailRule = EmailValidator.getInstance(false);
    private final RegexValidator passwordRule = new RegexValidator("^.{8,}$", false);

    @Override
    public boolean supports(Class<?> aClass) {
        return ProfileEditForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProfileEditForm form = (ProfileEditForm) o;
        checkEmail(form, errors);
        checkPassword(form, errors);
    }

    private void checkEmail(ProfileEditForm form, Errors errors) {
        // Check if the e-mail address is not empty.
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "email", "NotEmpty.email");
        if (errors.hasFieldErrors("email"))
            return;

        // Check if the e-mail address is valid.
        if (!emailRule.isValid(form.getEmail())) {
            errors.rejectValue("email", "Pattern.email");
            return;
        }

        // Skip if the e-mail address isn't modified.
        if (form.getEmail().equals(form.getCurrentAccount().getEmail()))
            return;
        // Check if the e-mail address hasn't been registered by another user.
        if (accountDao.findByEmail(form.getEmail()).isPresent()) {
            errors.rejectValue("email", "Unique.email");
        }
    }

    private void checkPassword(ProfileEditForm form, Errors errors) {
        // Skip checking if a new password isn't given.
        if (form.getPassword().isEmpty())
            return;

        // Check if the given current password is valid.
        ValidationUtils.rejectIfEmptyOrWhitespace(
                errors, "currentPassword", "NotEmpty.currentPassword");
        if (errors.hasFieldErrors("currentPassword"))
            return;
        if (!form.getCurrentAccount().isValidPassword(form.getCurrentPassword())) {
            errors.rejectValue("currentPassword", "EqualTo.currentPassword");
            return;
        }

        // Check if the new password is valid.
        if (!passwordRule.isValid(form.getPassword())) {
            errors.rejectValue("password", "Pattern.password");
            return;
        }
        if (!form.isValidRetypedPassword()) {
            errors.rejectValue("retypedPassword", "EqualTo.retypedPassword");
        }
    }
}
