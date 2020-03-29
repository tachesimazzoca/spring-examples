package com.github.tachesimazzoca.spring.examples.forum.domain.model.account;

import com.github.tachesimazzoca.spring.examples.forum.domain.shared.ValueObject;

public enum AccountStatus implements ValueObject<AccountStatus> {

    INACTIVE(0), ACTIVE(1);

    private int value;

    private AccountStatus() {
    }

    private AccountStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static AccountStatus of(int v) {
        for (AccountStatus s : AccountStatus.values()) {
            if (s.getValue() == v) {
                return s;
            }
        }
        throw new IllegalArgumentException("unknown value: " + v);
    }

    @Override
    public boolean sameValueAs(AccountStatus other) {
        return this == other;
    }
}
