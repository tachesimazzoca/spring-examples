package com.github.tachesimazzoca.spring.examples.forum.views;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

public class AccountsEntryForm {
    @NotEmpty(message = "{Account.email.NotEmpty}")
    @Email(message = "{Account.email.Email}")
    private String email = "";

    @NotEmpty(message = "{Account.password.NotEmpty}")
    @Pattern(
            regexp = "^(|\\p{Graph}+)$",
            message = "{Account.password.Pattern}")
    private String password = "";

    private String retypedPassword = "";

    @AssertTrue(message = "{Account.uniqueEmail.AssertTrue}")
    private boolean uniqueEmail = true;

    public String getEmail() {
        return email;
    }

    public void setEmail(String v) {
        this.email = v;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String v) {
        this.password = v;
    }

    public String getRetypedPassword() {
        return retypedPassword;
    }

    public void setRetypedPassword(String v) {
        this.retypedPassword = v;
    }

    public boolean isUniqueEmail() {
        return uniqueEmail;
    }

    public void setUniqueEmail(boolean uniqueEmail) {
        this.uniqueEmail = uniqueEmail;
    }

    @AssertTrue(message = "{Account.validRetypedPassword.AssertTrue}")
    public boolean isValidRetypedPassword() {
        if (password == null)
            return retypedPassword == null;
        else
            return password.equals(retypedPassword);
    }

    public static String[] getAllowedFields() {
        return new String[] {"email", "password", "retypedPassword"};
    }

    public static AccountsEntryForm emptyForm() {
        return new AccountsEntryForm();
    }

    public static AccountsEntryForm defaultForm() {
        return new AccountsEntryForm();
    }
}
