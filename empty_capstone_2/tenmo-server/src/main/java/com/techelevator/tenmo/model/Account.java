package com.techelevator.tenmo.model;

public class Account {
    private int accountId;
    private int userId;
    private double accountBalance;

    public double getAccountBalance() {
        return accountBalance;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Account(int accountId, int userId, double accountBalance) {
        this.accountId = accountId;
        this.userId = userId;
        this.accountBalance = accountBalance;
    }

    public Account() {

    }
}
