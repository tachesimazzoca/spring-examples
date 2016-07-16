package com.github.tachesimazzoca.spring.examples.forum.models;

import com.github.tachesimazzoca.spring.examples.forum.validation.CurrentPasswordRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.EmailRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.EqualRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.validation.NotEmptyRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.PatternRule;
import com.github.tachesimazzoca.spring.examples.forum.validation.UniqueEmailRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileEditFormValidator extends FormValidator {
    @Autowired
    public ProfileEditFormValidator(AccountDao accountDao) {
        super(ProfileEditForm.class,
                new NotEmptyRule("email"),
                new EmailRule("email"),
                new UniqueEmailRule(accountDao, "currentAccount", "email"),
                new CurrentPasswordRule("currentAccount", "password", "currentPassword"),
                new PatternRule("^.{6,}$", "password"),
                new EqualRule("password", "retypedPassword"));
    }
}
