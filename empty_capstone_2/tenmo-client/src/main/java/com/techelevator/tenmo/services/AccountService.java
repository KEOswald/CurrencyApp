package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Deposits;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.util.Scanner;


public class AccountService {

    private static final RestTemplate restTemplate = new RestTemplate();

    private static final String API_BASE_URL = "http://localhost:8080/";


   private AuthenticatedUser currentUser;

    public AccountService(String apiBaseUrl, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public AccountService(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public double getCurrentUserBalance() {
        double balance = 0; // Initialize balance to 0 or any default value
        try {
            int accountId = 2002; // Replace 2001 with the actual account ID
            String url = API_BASE_URL + "account/" + accountId;
            balance = restTemplate.getForObject(url, double.class);
        } catch (RestClientException e) {
            BasicLogger.log("Error occurred while fetching balance: " + e.getMessage());
            e.printStackTrace();
        }
        return balance;
    }

    public void sendBucks() {
        Transfers transfer = new Transfers();
        Scanner scanner = new Scanner(System.in);

        try {
            int recipientId = 1006;  // User ID for the "Wallet"
            transfer.setAccountTo(recipientId);
            transfer.setAccountFrom(currentUser.getUser().getId());

            System.out.println("Enter the amount you would like to transfer:");
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            transfer.setAmount(amount);

            restTemplate.postForObject(API_BASE_URL + "account/transfer", transfer, Transfers.class);

            System.out.println("Your transfer was successful");
        } catch (Exception e) {
            BasicLogger.log("Transfer failed: " + e.getMessage());
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }

    public void depositMoney() {
        Deposits deposit = new Deposits();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Enter the amount you would like to deposit:");
            BigDecimal amount = new BigDecimal(scanner.nextLine());


            deposit.setAccountId(currentUser.getUser().getId());
            deposit.setAmount(amount);

            restTemplate.postForObject(API_BASE_URL + "account/deposit", deposit, Void.class);

            System.out.println("Your deposit was successful");
        } catch (Exception e) {
            BasicLogger.log("Deposit failed: " + e.getMessage());
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

}
