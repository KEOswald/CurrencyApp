package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    Transfer createTransfer(int userFromId, int userToId, BigDecimal amount);

    List<Transfer> getTransfersByUserId(int userId);

    Transfer getTransferById(int transferId);

    void deposit(int accountId, BigDecimal amount);

    void withdraw(int accountId, BigDecimal amount);

    void walletDeposit(int accountId, BigDecimal amount);

    //void charge(int accountId, BigDecimal amount);


}


