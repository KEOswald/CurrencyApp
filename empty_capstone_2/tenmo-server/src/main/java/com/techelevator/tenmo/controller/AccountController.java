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
@RequestMapping(value = "/account/transfer", method = RequestMethod.POST)
    public Transfer getAccountTransfer(@RequestBody TransferDTO transferDTO, Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal not found");
        }

        User currentUser = userDao.getUserByUsername(principal.getName());
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Assuming 'User' object contains 'getId()' method to retrieve the user ID
        int userFromId = currentUser.getId();

        // Here you might also want to perform additional validations, such as ensuring userFromId matches transferDTO.getAccountFrom(),
        // and checking if the user has sufficient balance, etc.

        // Now proceed with creating the transfer
        return transferDao.createTransfer(userFromId, transferDTO.getAccountTo(), transferDTO.getAmount());
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

}
