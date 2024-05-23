package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private static final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private static AccountService accountService;  // No need to initialize here
    private final CurrencyService currencyService = new CurrencyService(API_BASE_URL);

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            accountService = new AccountService(API_BASE_URL, currentUser);
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser != null) {
            accountService = new AccountService(API_BASE_URL, currentUser); // Ensure accountService uses the correct user
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                displayCurrencies();
            } else if (menuSelection == 2) {
                displayAllCurrenciesByRegion();
            } else if (menuSelection == 3) {
                viewCurrentBalance();
            } else if (menuSelection == 4) {
                makeDeposit();
            } else if (menuSelection == 5) {
                displayCurrencyDetails();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }


    private void viewCurrentBalance() {
        if (currentUser != null) {
            try {
                double balance = accountService.getCurrentUserBalance();
                System.out.println("Current balance : $" + balance);
            } catch (Exception e) {
                System.out.println("Failed to get Balance");
            }
        }
    }

    private void displayCurrencyDetails() {
        currencyService.displayCurrencyDetails();
    }

    private void displayCurrencies() {
        currencyService.displayAllCurrencies(); // Call the displayAllCurrencies method from CurrencyService
    }

    private void displayAllCurrenciesByRegion() {
        Map<Integer, String> regionOptions = new HashMap<>();
        regionOptions.put(1, "Europe");
        regionOptions.put(2, "North America");
        regionOptions.put(3, "South America");
        regionOptions.put(4, "Africa");
        regionOptions.put(5, "Oceania");
        regionOptions.put(6, "Caribbean");
        regionOptions.put(7, "Middle East");
        regionOptions.put(8, "Central America");
        regionOptions.put(9, "Crypto & Commodity");
        regionOptions.put(10, "Unknown");

        System.out.println("Select a region:");

        for (Map.Entry<Integer, String> entry : regionOptions.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        int choice = consoleService.promptForInt("Enter your choice: ");

        if (regionOptions.containsKey(choice)) {
            String selectedRegion = regionOptions.get(choice);
            currencyService.displayCurrenciesByRegion(selectedRegion);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void makeDeposit() {
        BigDecimal amount = new BigDecimal(consoleService.promptForString("Enter the amount you would like to deposit: "));
        accountService.depositMoney(amount);

        // Fetch and display updated balance
        double balance = accountService.getCurrentUserBalance();
        System.out.println("Current balance : $" + balance);
    }

    private void sendBucks() {
        AccountService accountService = new AccountService(API_BASE_URL + "/account/transfer", currentUser);
        accountService.sendBucks();
    }

}



    /*
    private void sendBucks() {
        if (currentUser != null) {
            // Assuming AuthenticatedUser contains a User object
            User user = currentUser.getUser();
            if (user != null) {
                // Create a Principal object using the username from the User object
                Principal principal = new Principal() {
                    @Override
                    public String getName() {
                        return user.getUsername();
                    }
                };
                // Call sendBucks() method in accountService with the Principal object
                accountService.sendBucks(principal);
            } else {
                System.out.println("User information not available.");
            }
        } else {
            System.out.println("User not authenticated.");
        }
    }
    /*
     */

