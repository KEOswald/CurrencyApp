package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.DepositDTO;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransferDao transferDao;

    public void AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }
  //  @ResponseStatus(HttpStatus.ACCEPTED)
   // @RequestMapping(value = "/account/{account_id}", method = RequestMethod.GET)
   // public double getAccountBalance(@PathVariable("account_id") int accountId) {
  //      Account account = accountDao.getAccountById(accountId);
  //      return account.getAccountBalance();
  //  }

@ResponseStatus(HttpStatus.ACCEPTED)
@RequestMapping(value = "/account/{user_id}", method = RequestMethod.GET)
    public double getAccountBalance(@PathVariable("user_id") int userId) {
    Account account = accountDao.getAccountById(userId);
    return account.getAccountBalance();
    }

@ResponseStatus(HttpStatus.ACCEPTED)
@RequestMapping(value = "/account/transfer", method = RequestMethod.POST)
public Transfer getAccountTransfer(@RequestBody TransferDTO transferDTO) {
    return transferDao.createTransfer(transferDTO.getAccountFrom(), transferDTO.getAccountTo(), transferDTO.getAmount());
}

@ResponseStatus(HttpStatus.ACCEPTED)
@RequestMapping(value = "/account/deposit", method = RequestMethod.POST)
public void deposit(@RequestBody DepositDTO depositDTO) {
        transferDao.deposit(depositDTO.getAccountId(), depositDTO.getAmount());
    }

}
