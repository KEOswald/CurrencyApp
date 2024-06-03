package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private UserDao userDao;

    public void AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }


@ResponseStatus(HttpStatus.ACCEPTED)
@RequestMapping(value = "/account", method = RequestMethod.GET)
    public double getAccountBalance(Principal principal) {
    User getUser = userDao.getUserByUsername(principal.getName());
    Account account = accountDao.getAccountByUserId(getUser.getId());
    return account.getAccountBalance();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<RecipientsDTO> getUsers() {
        return userDao.listUsers();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/account/wallet", method = RequestMethod.POST)
    public void walletDeposit(@RequestBody DepositDTO depositDTO, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal not found");
        }

        User getUser = userDao.getUserByUsername(principal.getName());
        if (getUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        int userId = getUser.getId();
        transferDao.walletDeposit(userId, depositDTO.getAmount());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/account/deposit", method = RequestMethod.POST)
    public void deposit(@RequestBody DepositDTO depositDTO, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal not found");
        }

        User getUser = userDao.getUserByUsername(principal.getName());
        if (getUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        int userId = getUser.getId();
        transferDao.deposit(userId, depositDTO.getAmount());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/account/withdraw", method = RequestMethod.POST)
    public void withdraw(@RequestBody WithdrawDTO withdrawDTO, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal not found");
        }

        User getUser = userDao.getUserByUsername(principal.getName());
        if (getUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        int userId = getUser.getId();
        transferDao.withdraw(userId, withdrawDTO.getAmount());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/account/cashout", method = RequestMethod.POST)
    public void cashOut(@RequestBody WithdrawDTO withdrawDTO, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal not found");
        }

        User getUser = userDao.getUserByUsername(principal.getName());
        if (getUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        int userId = getUser.getId();
        transferDao.withdraw(userId, withdrawDTO.getAmount());
    }
}
