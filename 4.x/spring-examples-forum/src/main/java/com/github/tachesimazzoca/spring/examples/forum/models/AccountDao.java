package com.github.tachesimazzoca.spring.examples.forum.models;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class AccountDao {
    private static final AccountRowMapper ACCOUNT_ROW_MAPPER = new AccountRowMapper();

    private final NamedParameterJdbcTemplate adapter;
    private final SimpleJdbcInsert updater;

    public AccountDao(DataSource ds) {
        adapter = new NamedParameterJdbcTemplate(ds);
        updater = new SimpleJdbcInsert(ds)
                .withTableName("accounts")
                .usingColumns("email", "password_salt", "password_hash",
                        "nickname", "status")
                .usingGeneratedKeyColumns("id");
    }

    public Account find(Long id) {
        List<Account> accountList = adapter.query("SELECT * FROM accounts WHERE id = :id",
                new MapSqlParameterSource("id", id), ACCOUNT_ROW_MAPPER);
        if (accountList.isEmpty())
            throw new NoSuchElementException();
        return accountList.get(0);
    }

    public Account create(Account account) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", account.email)
                .addValue("password_salt", account.passwordSalt)
                .addValue("password_hash", account.passwordHash)
                .addValue("nickname", account.nickname)
                .addValue("status", account.status.getValue());
        Long id = (Long) updater.executeAndReturnKey(params);
        return find(id);
    }

    public Account update(Account account) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", account.email)
                .addValue("password_salt", account.passwordSalt)
                .addValue("password_hash", account.passwordHash)
                .addValue("nickname", account.nickname)
                .addValue("status", account.status.getValue())
                .addValue("id", account.id);
        adapter.update("UPDATE accounts"
                + " SET email = :email"
                + ", password_salt = :password_salt"
                + ", password_hash = :password_hash"
                + ", nickname = :nickname"
                + ", status = :status"
                + " WHERE id = :id", params);
        return find(account.id);
    }

    private static final class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Account(
                    resultSet.getLong("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password_salt"),
                    resultSet.getString("password_hash"),
                    resultSet.getString("nickname"),
                    Account.Status.fromValue(resultSet.getInt("status")));
        }
    }
}
