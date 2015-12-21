package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class AccountsEntryForm {
    private String email = "";
    private String password = "";
    private String retypedPassword = "";

    public void setEmail(String email) {
        this.email = StringUtils.trimToEmpty(email);
    }

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
