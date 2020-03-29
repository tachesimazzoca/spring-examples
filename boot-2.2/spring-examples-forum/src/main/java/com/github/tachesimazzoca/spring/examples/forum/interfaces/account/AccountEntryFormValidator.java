package com.github.tachesimazzoca.spring.examples.forum.interfaces.account;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.tachesimazzoca.spring.examples.forum.domain.model.account.AccountRepository;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation.EmailChecker;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation.EqualChecker;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation.FormValidator;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation.PasswordChecker;
import com.github.tachesimazzoca.spring.examples.forum.interfaces.shared.validation.UniqueAccountEmailChecker;

@Component
public class AccountEntryFormValidator extends FormValidator {

    @Autowired
    public AccountEntryFormValidator(AccountRepository accountRepository) {
        setAssignableClass(AccountEntryForm.class);
        addRule("email", new EmailChecker());
        addRule("email", new UniqueAccountEmailChecker(accountRepository, Optional.empty()));
        addRule("password", new PasswordChecker());
        addRule("retypedPassword", new EqualChecker("password"));
    }
}
