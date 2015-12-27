package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class AccountsLoginForm {
    private String email = "";
    private String password = "";
    private String returnTo = "";

    public static String[] getAllowedFields() {
        return new String[]{"email", "password", "returnTo"};
    }
}
