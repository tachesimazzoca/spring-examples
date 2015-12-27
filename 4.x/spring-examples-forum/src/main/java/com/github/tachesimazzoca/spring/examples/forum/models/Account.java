package com.github.tachesimazzoca.spring.examples.forum.models;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

@Data
public class Account {
    private static final int PASSWORD_SALT_LENGTH = 4;
    private Long id;
    private String email;
    private String passwordSalt;
    private String passwordHash;
    private String nickname;
    private Status status;

    public void refreshPassword(String password) {
        setPasswordSalt(RandomStringUtils.randomAlphanumeric(PASSWORD_SALT_LENGTH));
        setPasswordHash(hashPassword(password, getPasswordSalt()));
    }

    public void refreshPassword(String password, String salt) {
        if (salt == null || salt.length() != PASSWORD_SALT_LENGTH)
            throw new IllegalArgumentException(
                    "The length of the parameter salt must be equal to " + PASSWORD_SALT_LENGTH);
        setPasswordSalt(salt);
        setPasswordHash(hashPassword(password, getPasswordSalt()));
    }

    public boolean isValidPassword(String password) {
        return hashPassword(password, getPasswordSalt()).equals(getPasswordHash());
    }

    private String hashPassword(String password, String salt) {
        return DigestUtils.sha1Hex(salt + password);
    }

    public enum Status {
        INACTIVE(0), ACTIVE(1);

        private int value;

        private Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Status fromValue(int v) {
            for (Status s : Status.values()) {
                if (s.getValue() == v) {
                    return s;
                }
            }
            throw new IllegalArgumentException("unknown value: " + v);
        }
    }
}
