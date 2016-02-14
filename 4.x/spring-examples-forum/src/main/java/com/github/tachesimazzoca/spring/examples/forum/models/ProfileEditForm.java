package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

@Data
public class ProfileEditForm {
    private String email = "";
    private String currentPassword = "";
    private String password = "";
    private String retypedPassword = "";
    private String nickname = "";
    private String iconToken = "";

    private Account currentAccount;

    public boolean isValidRetypedPassword() {
        if (null == password)
            return null == retypedPassword;
        else
            return password.equals(retypedPassword);
    }

    public static String[] getAllowedFields() {
        return new String[]{"email", "currentPassword",
                "password", "retypedPassword", "nickname",
                "iconToken"};
    }
}
