package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class jdbcAccountDao implements AccountDao {

private final JdbcTemplate jdbcTemplate;

public jdbcAccountDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
}
    @Override
    public Account getAccountById(int accountId) {
       String sql = "Select account_id, user_id, balance FROM Account WHERE account_id = ?";
       SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
       if (results.next()) {
           return mapRowToAccount(results);
       } else {
           return null;
       }
    }

    @Override
    public Account getAccountByUserId(int userId) {
    String sql = "Select account_id, user_id, balance FROM Account WHERE user_id = ?";
    SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
    if (results.next()) {
        return mapRowToAccount(results);
    } else {
        return null;
        }
    }

    private Account mapRowToAccount(SqlRowSet results) {
        Account account = new Account();
        account.setAccountId(results.getInt("account_id")); // Set the account ID
        account.setUserId(results.getInt("user_id"));
        account.setAccountBalance(results.getDouble("balance"));
        return account;
    }


}
