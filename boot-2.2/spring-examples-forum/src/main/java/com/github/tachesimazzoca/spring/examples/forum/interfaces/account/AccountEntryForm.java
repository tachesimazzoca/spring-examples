package com.github.tachesimazzoca.spring.examples.forum.interfaces.account;

import org.apache.commons.lang3.StringUtils;
import lombok.Data;

@Data
public class AccountEntryForm {

    private String email = "";

    private String password = "";

    private String retypedPassword = "";

    public void setEmail(String email) {
        this.email = StringUtils.trimToEmpty(email);
    }

    public boolean isValidRetypedPassword() {
        if (password == null) {
            return retypedPassword == null;
        } else {
            return password.equals(retypedPassword);
        }
    }
}
