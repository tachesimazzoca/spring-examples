package com.github.tachesimazzoca.spring.examples.forum.validation;

import com.github.tachesimazzoca.spring.examples.forum.models.Account;
import com.github.tachesimazzoca.spring.examples.forum.models.AccountDao;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswersEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.AnswersEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.ProfileEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.ProfileEditFormValidator;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionsEditForm;
import com.github.tachesimazzoca.spring.examples.forum.models.QuestionsEditFormValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import static com.github.tachesimazzoca.spring.examples.forum.util.ParameterUtils.params;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring/test-application.xml")
public class FormValidatorTest {
    @Test
    public void testQuestionsEditFormValidator() {
        final FormValidator validator = new QuestionsEditFormValidator();

        final QuestionsEditForm form = new QuestionsEditForm();
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
        final FormValidator validator = new AnswersEditFormValidator();

        final AnswersEditForm form = new AnswersEditForm();
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
        Mockito.when(accountDao.findByEmail(Mockito.anyString())).thenReturn(null);
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
}
