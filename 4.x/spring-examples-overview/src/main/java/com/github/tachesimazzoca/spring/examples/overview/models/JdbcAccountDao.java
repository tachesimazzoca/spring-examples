package com.github.tachesimazzoca.spring.examples.overview.models;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate adapter;
    private  static final AccountRowMapper ACCOUNT_ROW_MAPPER = new AccountRowMapper();

    public JdbcAccountDao(DataSource ds) {
        adapter = new JdbcTemplate(ds);
    }

    @Override
    public Account find(Long id) {
        List<Account> accountList = adapter.query(
                "SELECT * FROM accounts WHERE id = ?", ACCOUNT_ROW_MAPPER, id);
        if (accountList.isEmpty()) {
            return null;
        } else {
            return accountList.get(0);
        }
    }

    private static final class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet resultSet, int i) throws SQLException {
            Long id = resultSet.getLong("id");
            String email = resultSet.getString("email");
            Account.Status status = Account.Status.fromValue(resultSet.getInt("status"));
            return new Account(id, email, status);
        }
    }
}
