package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;


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
    @RequestMapping(value = "/account/transfers", method = RequestMethod.POST)
    public Transfer getAccountTransfer(@RequestBody TransferDTO transferDTO, Principal principal,
                                       @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal not found");
        }

        // Extract the token from the Authorization header
        String token = extractToken(authorizationHeader);

        // Validate the token (e.g., decode, verify signature, etc.)

        User currentUser = userDao.getUserByUsername(principal.getName());
        if (currentUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        int userFromId = currentUser.getId();

        // Perform the transfer operation
        return transferDao.createTransfer(userFromId, transferDTO.getAccountTo(), transferDTO.getAmount());
    }

    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/account/transfer", method = RequestMethod.POST)
    public Transfer getTransfer(@RequestBody TransferDTO transferDTO) {
        return transferDao.createTransfer(transferDTO.getAccountFrom(), transferDTO.getAccountTo(), transferDTO.getAmount());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/account/transfer/{id}", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(@PathVariable int id){
        List<Transfer> transferList = transferDao.getTransfersByUserId(id);
        return transferList;
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(value = "/account/transfer/details/{id}", method = RequestMethod.GET)
    public Transfer getTransferDetails(@PathVariable int id) {
        return transferDao.getTransferById(id);
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
