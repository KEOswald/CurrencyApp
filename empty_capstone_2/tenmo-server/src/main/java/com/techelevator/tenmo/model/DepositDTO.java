package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class DepositDTO {
    private int accountId;
    private BigDecimal amount;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
