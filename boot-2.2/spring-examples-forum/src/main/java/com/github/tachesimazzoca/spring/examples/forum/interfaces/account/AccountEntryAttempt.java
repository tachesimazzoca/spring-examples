package com.github.tachesimazzoca.spring.examples.forum.interfaces.account;

import java.io.Serializable;
import lombok.Data;

@Data
public class AccountEntryAttempt implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sessionId;

    private String email;
}
