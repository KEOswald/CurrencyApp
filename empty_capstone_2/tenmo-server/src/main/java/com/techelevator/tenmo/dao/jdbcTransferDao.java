package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
    public class jdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public jdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private BigDecimal getbalance(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
    }

    private int getaccountID(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        if (accountId == null) {
            throw new IllegalArgumentException("Account not found for user ID: " + userId);
        }
        return accountId;
    }

    @Override
    public void cashOut(int userId) {
        // Fetch the account ID associated with the user ID
        int accountId = getAccountIdByUserId(userId);
        if (accountId == 0) {
            throw new IllegalArgumentException("Account not found for user ID: " + userId);
        }

        // Fetch the current balance of the account
        BigDecimal currentBalance = getbalance(accountId);

        // Check if the account has a positive balance
        if (currentBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("No funds available for cash out");
        }

        // Update the account balance to zero
        String sqlUpdateBalance = "UPDATE account SET balance = 0 WHERE account_id = ?";
        jdbcTemplate.update(sqlUpdateBalance, accountId);
    }

    @Override
    public void withdraw(int userId, BigDecimal amount) throws IllegalArgumentException {
        // Check if the withdrawal amount is negative or zero
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount cannot be negative or zero");
        }

        // Fetch the account ID associated with the user ID
        int accountId = getAccountIdByUserId(userId);
        if (accountId == 0) {
            throw new IllegalArgumentException("Account not found for user ID: " + userId);
        }

        // Fetch the current balance of the account
        BigDecimal currentBalance = getbalance(accountId);

        // Check if the account has enough money for the withdrawal
        if (currentBalance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds for withdrawal");
        }

        // Update the account balance
        String sqlUpdateBalance = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
        jdbcTemplate.update(sqlUpdateBalance, amount, accountId);
    }

    @Override
    public void deposit(int userId, BigDecimal amount) {

        // Fetch the account ID associated with the user ID
        int accountId = getAccountIdByUserId(userId);
        if (accountId == 0) {
            throw new IllegalArgumentException("Account not found for user ID: " + userId);
        }

        // Update the account balance
        String sqlUpdateBalance = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        jdbcTemplate.update(sqlUpdateBalance, amount, accountId);
    }

    @Override
    public void walletDeposit(int userId, BigDecimal amount) {

        // Fetch the account ID associated with the user ID
        int accountId = getAccountIdByUserId(1006);


        // Update the account balance
        String sqlUpdateBalance = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        jdbcTemplate.update(sqlUpdateBalance, amount, accountId);
    }

    private int getAccountIdByUserId(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }
}


