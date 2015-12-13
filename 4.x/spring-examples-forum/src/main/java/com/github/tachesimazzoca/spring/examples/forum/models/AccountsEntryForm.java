package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

@Data
public class AccountsEntryForm {
    private String email = "";
    private String password = "";
    private String retypedPassword = "";
    private boolean uniqueEmail = true;

    public boolean isValidRetypedPassword() {
        if (password == null)
            return retypedPassword == null;
        else
            return password.equals(retypedPassword);
    }

    public static String[] getAllowedFields() {
        return new String[]{"email", "password", "retypedPassword"};
    }
}
