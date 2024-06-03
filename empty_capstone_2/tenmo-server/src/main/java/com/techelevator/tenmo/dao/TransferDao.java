package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    void deposit(int accountId, BigDecimal amount);

    void cashOut(int userId);

    void withdraw(int accountId, BigDecimal amount);

    void walletDeposit(int accountId, BigDecimal amount);




}


