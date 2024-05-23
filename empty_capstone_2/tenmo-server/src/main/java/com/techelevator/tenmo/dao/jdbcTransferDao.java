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
        public Transfer createTransfer(int userFromId, int userToId, BigDecimal amount) {

            int accountFromId = getaccountID(userFromId);
            int accountToId = getaccountID(userToId);

          //  BigDecimal fee = calculateFee(amount);
          //  charge(userFromId, fee);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("transfer amount cannot be negative");
            }

            BigDecimal sendValue = getbalance(accountFromId);
            if (sendValue.compareTo(amount) < 0) {
                throw new IllegalArgumentException("Insufficient balance");
            }

            String sqlInsertTransfer = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (2, 2, ?, ?, ?) RETURNING transfer_id";

            int transferId = jdbcTemplate.queryForObject(sqlInsertTransfer, Integer.class, accountFromId, accountToId, amount);

            // deduct the amount from the sender's account
            String sqlUpdateSenderBalance = "UPDATE account SET balance = balance - ? WHERE account_id = ?";
            jdbcTemplate.update(sqlUpdateSenderBalance, amount, accountFromId);

            // add the amount to the recipient's account
            String sqlUpdateRecipientBalance = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
            jdbcTemplate.update(sqlUpdateRecipientBalance, amount, accountToId);

            // get the created transfer details
            return getTransferById(transferId);
        }

    private BigDecimal calculateFee(BigDecimal amount) {
        // Define your fee calculation logic here (e.g., 1% of the amount)
        return amount.multiply(BigDecimal.valueOf(0.01));
    }


    @Override
    public void deposit(int userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative");
        }

        // Fetch the account ID associated with the user ID
        int accountId = getAccountIdByUserId(userId);
        if (accountId == 0) {
            throw new IllegalArgumentException("Account not found for user ID: " + userId);
        }

        String sqlUpdateBalance = "UPDATE account SET balance = balance + ? WHERE account_id = ?";
        jdbcTemplate.update(sqlUpdateBalance, amount, accountId);
    }

    private int getAccountIdByUserId(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId);
    }

        @Override
        public List<Transfer> getTransfersByUserId(int userId) {
            List<Transfer> result = new ArrayList<>();
            String sql = "SELECT * FROM transfer WHERE account_from = ? OR account_to = ?";
            SqlRowSet transferTrans = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while(transferTrans.next()) {
                Transfer transfer = mapRowToTransfer(transferTrans);
                result.add(transfer);
            }
            return result;
        }

    @Override
    public Transfer getTransferById(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        } else {
            return null;
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet transferTrans) {
            Transfer transfer = new Transfer();
            transfer.setTransferId(transferTrans.getInt("transfer_id"));
            transfer.setTransferTypeId(transferTrans.getInt("transfer_type_id"));
            transfer.setTransferStatusId(transferTrans.getInt("transfer_status_id"));
            transfer.setAccountFrom(transferTrans.getInt("account_from"));
            transfer.setAccountTo(transferTrans.getInt("account_to"));
            transfer.setAmount(transferTrans.getBigDecimal("amount"));
            return transfer;
        }

    }
