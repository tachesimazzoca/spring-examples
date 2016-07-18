package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountEntryForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountEntryFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswerEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.ProfileEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.ProfileEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.RecoveryEntryForm;
import com.github.tachesimazzoca.spring.examples.forum.models.RecoveryEntryFormValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import java.util.Optional;

import static com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils.params;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class FormValidatorTest {
    @Test
    public void testAccountsEntryFormValidator() {
        AccountDao accountDao = Mockito.mock(AccountDao.class);
        Mockito.when(accountDao.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        final FormValidator validator = new AccountEntryFormValidator(accountDao);

        final AccountEntryForm form = new AccountEntryForm();
        assertTrue(validator.supports(form.getClass()));
        final Errors errors = new MapBindingResult(
                params(
                        "email", "user@example.net",
                        "password", "",
                        "retypedPassword", "a"
                ), "accountsEditForm");
        validator.validate(form, errors);
        assertTrue(errors.hasErrors());
        assertTrue(errors.hasFieldErrors("password"));
        assertTrue(errors.hasFieldErrors("retypedPassword"));
    }

    @Test
    public void testQuestionsEditFormValidator() {
        final FormValidator validator = new QuestionEditFormValidator();

        final QuestionEditForm form = new QuestionEditForm();
        assertTrue(validator.supports(form.getClass()));
        final Errors errors = new MapBindingResult(
                params("subject", "How does it works?", "body", ""),
                "questionsEditForm");
        validator.validate(form, errors);
        assertTrue(errors.hasFieldErrors());
        assertEquals(1, errors.getErrorCount());
        assertFalse(errors.hasFieldErrors("subject"));
        assertTrue(errors.hasFieldErrors("body"));
    }

    @Test
    public void testAnswersEditFormValidator() {
        final FormValidator validator = new AnswerEditFormValidator();

        final AnswerEditForm form = new AnswerEditForm();
        assertTrue(validator.supports(form.getClass()));
        final Errors errors = new MapBindingResult(
                params("body", ""), "answersEditForm");
        validator.validate(form, errors);
        assertTrue(errors.hasFieldErrors());
        assertEquals(1, errors.getErrorCount());
        assertTrue(errors.hasFieldErrors("body"));
    }

    @Test
    public void testProfileEditFormValidator() {
        AccountDao accountDao = Mockito.mock(AccountDao.class);
        Mockito.when(accountDao.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        final FormValidator validator = new ProfileEditFormValidator(accountDao);

        final ProfileEditForm form = new ProfileEditForm();
        Account currentAccount = new Account();
        currentAccount.setEmail("user1@example.net");
        currentAccount.refreshPassword("deadbeef");
        form.setCurrentAccount(currentAccount);
        assertTrue(validator.supports(form.getClass()));
        final Errors errors = new MapBindingResult(
                params(
                        "email", currentAccount.getEmail(),
                        "currentPassword", "deadbeef",
                        "password", "deadbeef2",
                        "retypedPassword", "deadbeef2"
                ), "profileEditForm");
        validator.validate(form, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testRecoveryEntryFormValidator() {
        AccountDao accountDao = Mockito.mock(AccountDao.class);
        Account account = new Account();
        account.setEmail("recovered-user@example.net");
        account.setStatus(Account.Status.ACTIVE);
        Mockito.when(accountDao.findByEmail(Mockito.anyString())).thenReturn(Optional.of(account));
        final FormValidator validator = new RecoveryEntryFormValidator(accountDao);

        final RecoveryEntryForm form = new RecoveryEntryForm();
        assertTrue(validator.supports(form.getClass()));
        final Errors errors = new MapBindingResult(
                params("email", account.getEmail()), "recoveryEntryForm");
        validator.validate(form, errors);
        assertFalse(errors.hasErrors());
    }
}
