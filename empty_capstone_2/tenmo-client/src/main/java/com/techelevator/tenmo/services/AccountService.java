package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Deposits;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
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
            int recipientId = 2003;
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
/*
    public void sendBucks(Principal principal) {
                Scanner scanner = new Scanner(System.in);

        try {
            if (principal == null) {
                throw new IllegalArgumentException("Principal not found");
            }

            // Create a User object using the username from the Principal
            User currentUser = new User();
            currentUser.setUsername(principal.getName());

            int recipientId = 1006;  // User ID for the "Wallet"
            int senderId = currentUser.getAccountId();

            System.out.println("Enter the amount you would like to transfer:");
            BigDecimal amount = new BigDecimal(scanner.nextLine());

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(AuthenticatedUser.getToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            Deposits deposit = new Deposits();
            deposit.setAccountId(senderId);
            deposit.setAmount(amount);

            HttpEntity<Deposits> entity = new HttpEntity<>(deposit, headers);
            restTemplate.postForEntity(apiUrl + "account/transfer", entity, Void.class);


            System.out.println("Your transfer was successful");
        } catch (Exception e) {
            BasicLogger.log("Transfer failed: " + e.getMessage());
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
    /*
 */
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

}
