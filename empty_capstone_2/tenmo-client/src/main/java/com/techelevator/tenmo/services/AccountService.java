package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.Scanner;


public class AccountService {

    private static final RestTemplate restTemplate = new RestTemplate();

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final String apiUrl;

    private AuthenticatedUser currentUser;


    public AccountService(String apiUrl, AuthenticatedUser currentUser) {
        this.apiUrl = apiUrl;
        this.currentUser = currentUser;
    }

    public double getCurrentUserBalance() {
        double balance = 0;
        try {
            String url = API_BASE_URL + "account";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(AuthenticatedUser.getToken()); // Use the token obtained from login

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Double> response = restTemplate.exchange(url, HttpMethod.GET, entity, Double.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                balance = response.getBody();
            }
        } catch (RestClientException e) {
            BasicLogger.log("Error occurred while fetching balance: " + e.getMessage());
            e.printStackTrace();
        }
        return balance;
    }

    public void sendBucks() {
        Transfers transfer = new Transfers();

        try {
            // Set recipient ID to 2003
            int recipientId = 1004;
            transfer.setAccountTo(recipientId);

            // Set accountFrom using the currentUser
            int accountFrom = currentUser.getUser().getId();
            transfer.setAccountFrom(accountFrom);

            System.out.println("Enter the amount you would like to transfer");
            Scanner scanner = new Scanner(System.in);
            BigDecimal amount = new BigDecimal(Double.parseDouble(scanner.nextLine()));
            transfer.setAmount(amount);

            // Send transfer request
            restTemplate.postForObject(API_BASE_URL + "account/transfers", transfer, Transfers.class);

            System.out.println("Your transfer was successful");
        } catch (Exception e) {
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
    public void depositMoneyToWallet(BigDecimal amount) {
        Deposits depositMoneyToWallet = new Deposits();
        depositMoneyToWallet.setAccountId(currentUser.getUser().getId());
        depositMoneyToWallet.setAmount(amount);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Deposits> entity = new HttpEntity<>(depositMoneyToWallet, headers);
            restTemplate.postForEntity(apiUrl + "account/wallet", entity, Void.class);

            System.out.println("Your deposit was successful");
        } catch (RestClientException e) {
            BasicLogger.log("Deposit failed: " + e.getMessage());
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }

    public void depositMoney(BigDecimal amount) {
        Deposits deposit = new Deposits();
        deposit.setAccountId(currentUser.getUser().getId());
        deposit.setAmount(amount);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Deposits> entity = new HttpEntity<>(deposit, headers);
            restTemplate.postForEntity(apiUrl + "account/deposit", entity, Void.class);

            System.out.println("Your deposit was successful");
        } catch (RestClientException e) {
            BasicLogger.log("Deposit failed: " + e.getMessage());
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }


    public void withdrawMoney(BigDecimal amount) {
        Withdraws withdraws = new Withdraws();
        withdraws.setAccountId(currentUser.getUser().getId());
        withdraws.setAmount(amount);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Withdraws> entity = new HttpEntity<>(withdraws, headers);
            restTemplate.postForEntity(apiUrl + "account/withdraw", entity, Void.class);

            System.out.println();
            System.out.println("Your withdraw was successful, converted money has been dispensed");
        } catch (RestClientException e) {
            BasicLogger.log("Withdraw failed: " + e.getMessage());
            System.out.println("Withdraw failed: " + e.getMessage());
        }
    }

}
