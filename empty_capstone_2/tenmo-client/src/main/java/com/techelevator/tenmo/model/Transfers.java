package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {
    private int accountFrom;

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    private int accountTo;
    private BigDecimal amount;
    private int transferId;
    private int transferStatusId;

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getTransferId() {
        return transferId;
    }


}
