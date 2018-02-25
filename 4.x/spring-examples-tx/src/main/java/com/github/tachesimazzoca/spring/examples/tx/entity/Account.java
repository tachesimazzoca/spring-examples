package com.github.tachesimazzoca.spring.examples.tx.entity;

public class Account {
    public final Long id;
    public final String username;
    public final java.util.Date modifiedAt;

    public Account(Long id, String username, java.util.Date modifiedAt) {
        this.id = id;
        this.username = username;

        this.modifiedAt = modifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (id != null ? !id.equals(account.id) : account.id != null) return false;
        if (username != null ? !username.equals(account.username) : account.username != null) return false;
        return modifiedAt != null ? modifiedAt.equals(account.modifiedAt) : account.modifiedAt == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (modifiedAt != null ? modifiedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("{ id:%d, username:%s, modifiedAt:%s }", id, username, modifiedAt);
    }
}
