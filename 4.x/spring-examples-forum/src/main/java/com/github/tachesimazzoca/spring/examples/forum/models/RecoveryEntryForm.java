package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class RecoveryEntryForm {
    private String email = "";

    public void setEmail(String email) {
        this.email = StringUtils.trimToEmpty(email);
    }

    public static String[] getAllowedFields() {
        return new String[]{"email"};
    }
}
