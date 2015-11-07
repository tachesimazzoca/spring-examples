package com.github.tachesimazzoca.spring.examples.forum.models;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AccountDao extends JdbcTemplateDao<Account> {
    public static final String TABLE_NAME = "accounts";
    public static final String[] COLUMNS = {
            "email", "password_salt", "password_hash", "nickname", "status" };
    public static final String GENERATED_KEY_COLUMN = "id";

    public AccountDao(DataSource dataSource) {
        super(dataSource, TABLE_NAME, COLUMNS, GENERATED_KEY_COLUMN);
    }

    public Optional<Account> findByEmail(String email) {
        List<Account> rowList = getJdbcTemplate().query(
                "SELECT * FROM accounts WHERE email = ?", getRowMapper(), email);
        if (!rowList.isEmpty()) {
            return Optional.of(rowList.get(0));
        } else {
            return Optional.empty();
        }
    }

    public Account save(Account account) {
        if (null == account.getId())
            return create(account);
        else
            return update(account);
    }

    @Override
    protected Map<String, ?> convertEntityToMap(Account entity) {
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("id", entity.getId());
        entityMap.put("email", entity.getEmail());
        entityMap.put("password_salt", entity.getPasswordSalt());
        entityMap.put("password_hash", entity.getPasswordHash());
        entityMap.put("nickname", entity.getNickname());
        entityMap.put("status", entity.getStatus().getValue());
        return entityMap;
    }

    @Override
    public Account convertResultSetToEntity(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getLong("id"));
        account.setEmail(resultSet.getString("email"));
        account.setPasswordSalt(resultSet.getString("password_salt"));
        account.setPasswordHash(resultSet.getString("password_hash"));
        account.setNickname(resultSet.getString("nickname"));
        account.setStatus(Account.Status.fromValue(resultSet.getInt("status")));
        return account;
    }
}
