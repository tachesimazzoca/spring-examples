package com.github.tachesimazzoca.spring.examples.overview;

import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import static org.junit.Assert.*;

public class ValidationUtilsTest {
    public class AccountForm {
        private String username;
        private String password;
        private String retypedPassword;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRetypedPassword() {
            return retypedPassword;
        }

        public void setRetypedPassword(String retypedPassword) {
            this.retypedPassword = retypedPassword;
        }
    }

    @Test
    public void testRejectIfEmpty() {
        AccountForm form = new AccountForm();
        Errors errors = new BeanPropertyBindingResult(form, "accountForm");
        form.setUsername("    ");
        form.setPassword("    ");
        form.setRetypedPassword("");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required");
        ValidationUtils.rejectIfEmpty(errors, "password", "required"); // Allow whitespace
        ValidationUtils.rejectIfEmpty(errors, "retypedPassword", "required");
        assertEquals(2, errors.getFieldErrors().size());
        assertEquals("required", errors.getFieldError("username").getCode());
        assertEquals("required", errors.getFieldError("retypedPassword").getCode());
    }
}
