package com.github.tachesimazzoca.spring.examples.forum.domain.model.account;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import com.github.tachesimazzoca.spring.examples.forum.domain.shared.Identifiable;
import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class Account implements Identifiable<Account> {

    private static final int PASSWORD_SALT_LENGTH = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long accountId;

    private String email;

    private String passwordSalt;

    private String passwordHash;

    private String nickname;

    private AccountStatus accountStatus;

    @Override
    public boolean sameIdentityAs(Account other) {
        return other != null && accountId.equals(other.getAccountId());
    }

    public boolean isActive() {
        return accountStatus.sameValueAs(AccountStatus.ACTIVE);
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

    public boolean isValidPassword(String password) {
        return hashPassword(password, getPasswordSalt()).equals(getPasswordHash());
    }

    private String hashPassword(String password, String salt) {
        return DigestUtils.sha1Hex(salt + password);
    }
}
