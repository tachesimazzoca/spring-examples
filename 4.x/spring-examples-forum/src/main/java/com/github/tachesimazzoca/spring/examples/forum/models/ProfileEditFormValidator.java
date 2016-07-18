package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.CurrentPasswordChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.EmailChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.EqualChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.PasswordChecker;
import com.github.tachesimazzoca.spring.examples.forum.validation.UniqueEmailChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProfileEditFormValidator extends FormValidator {
    @Autowired
    public ProfileEditFormValidator(AccountDao accountDao) {
        setAssignableClass(ProfileEditForm.class);
        addRule("email", new EmailChecker());
        addRule("email", new UniqueEmailChecker(accountDao, Optional.of("currentAccount")));
        addRule("currentPassword", new CurrentPasswordChecker("currentAccount", "password"));
        addRule("password", new PasswordChecker(false));
        addRule("retypedPassword", new EqualChecker("password"));
    }
}
