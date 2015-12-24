package com.github.tachesimazzoca.spring.examples.forum.sessions;

import lombok.Data;

@Data
public class UserSession {
    public static final String KEY = "user";

    private Long lastAccessedTime;
    private Long accountId;
}
