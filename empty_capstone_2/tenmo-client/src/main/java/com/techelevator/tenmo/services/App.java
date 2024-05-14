package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;

import java.util.HashMap;
import java.util.Map;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL, currentUser);
    private final CurrencyService currencyService = new CurrencyService(API_BASE_URL);

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
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
        if (currentUser == null) {
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
                AccountService service = new AccountService(API_BASE_URL, currentUser);
                double balance = service.getCurrentUserBalance();
                System.out.println("Current balance : $" + balance);
            } catch (Exception e) {
                System.out.println("Failed to get Balance");
            }
        }
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
    private void makeAnExchange() {

    }
    private void makeDeposit() {
        AccountService deposit = new AccountService(API_BASE_URL + "/account/deposit", currentUser);
        deposit.depositMoney();
    }
    private void sendBucks() {
        AccountService service = new AccountService(API_BASE_URL + "/account/transfer", currentUser);
        service.sendBucks();
    }

	private void viewExchangeHistory() {
		// TODO Auto-generated method stub

	}





}
