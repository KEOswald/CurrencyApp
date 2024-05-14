package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transferTypeId;

    public Transfer() {

    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

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

    private int transferStatusId;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;
    private int transferId;

    public Transfer(int transferTypeId, int transferStatusId, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
}
