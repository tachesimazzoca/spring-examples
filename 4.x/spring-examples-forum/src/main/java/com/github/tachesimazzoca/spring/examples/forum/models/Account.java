package com.github.tachesimazzoca.spring.examples.forum.models;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

public class Account {
    private static final int PASSWORD_SALT_LENGTH = 4;
    private Long id;
    private String email;
    private String passwordSalt;
    private String passwordHash;
    private String nickname;
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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

    public boolean isEqualPassword(String password) {
        return hashPassword(password, getPasswordSalt()).equals(getPasswordHash());
    }

    private String hashPassword(String password, String salt) {
        return DigestUtils.sha1Hex(salt + password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (email != null ? !email.equals(account.email) : account.email != null) return false;
        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (nickname != null ? !nickname.equals(account.nickname) : account.nickname != null) return false;
        if (passwordHash != null ? !passwordHash.equals(account.passwordHash) : account.passwordHash != null)
            return false;
        if (passwordSalt != null ? !passwordSalt.equals(account.passwordSalt) : account.passwordSalt != null)
            return false;
        if (status != account.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (passwordSalt != null ? passwordSalt.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
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
