package com.github.tachesimazzoca.spring.examples.forum.models;

public class Account {
    public final Long id;
    public final String email;
    public final String passwordSalt;
    public final String passwordHash;
    public final String nickname;
    public final Status status;

    public Account(Long id, String email, String passwordSalt, String passwordHash,
                   String nickname, Status status) {
        this.id = id;
        this.email = email;
        this.passwordSalt = passwordSalt;
        this.passwordHash = passwordHash;
        this.nickname = nickname;
        this.status = status;
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
