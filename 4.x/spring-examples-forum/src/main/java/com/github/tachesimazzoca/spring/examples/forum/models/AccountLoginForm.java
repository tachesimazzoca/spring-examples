package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;

@Data
public class AccountLoginForm {
    private String email = "";
    private String password = "";
    private String returnTo = "";

    public static String[] getAllowedFields() {
        return new String[]{"email", "password", "returnTo"};
    }
}
