package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

@Data
public class RecoveryResetForm {
    private String code = "";
    private String password = "";
    private String retypedPassword = "";

    public boolean isValidRetypedPassword() {
        if (password == null)
            return retypedPassword == null;
        else
            return password.equals(retypedPassword);
    }

    public static String[] getAllowedFields() {
        return new String[]{"password", "retypedPassword"};
    }
}
