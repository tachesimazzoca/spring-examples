package com.github.tachesimazzoca.spring.examples.forum.models;

import com.google.common.base.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountDao extends JdbcTemplateDao<Account> {
    private static final AccountRowMapper ACCOUNT_ROW_MAPPER = new AccountRowMapper();

    public AccountDao(DataSource ds) {
        super(new DataSourceTransactionManager(ds),
                new JdbcTemplate(ds),
                new SimpleJdbcInsert(ds)
                        .withTableName("accounts")
                        .usingColumns("email", "password_salt", "password_hash",
                                "nickname", "status")
                        .usingGeneratedKeyColumns("id"),
                ACCOUNT_ROW_MAPPER);
    }

    public Optional<Account> findByEmail(String email) {
        List<Account> rowList = getJdbcTemplate().query(
                "SELECT * FROM accounts WHERE email = ?", ACCOUNT_ROW_MAPPER, email);
        if (rowList.isEmpty()) {
            return Optional.absent();
        } else {
            return Optional.of(rowList.get(0));
        }
    }

    public Account save(Account account) {
        if (null == account.getId())
            return create(account);
        else
            return update(account);
    }

    @Override
    protected Map<String, ?> convertToMap(Account entity) {
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("id", entity.getId());
        entityMap.put("email", entity.getEmail());
        entityMap.put("password_salt", entity.getPasswordSalt());
        entityMap.put("password_hash", entity.getPasswordHash());
        entityMap.put("nickname", entity.getNickname());
        entityMap.put("status", entity.getStatus().getValue());
        return entityMap;
    }

    private static final class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet resultSet, int i) throws SQLException {
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
}
