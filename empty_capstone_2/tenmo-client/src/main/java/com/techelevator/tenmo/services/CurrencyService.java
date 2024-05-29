package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyService {
    private Scanner scanner;

    private final String baseUrl;
    private static final String BASE_CURRENCY_CODE = "USD"; // Base currency code
    private static final double BASE_CURRENCY_RATE = 1.0;



    public CurrencyService(String baseUrl) {
        this.baseUrl = baseUrl;
        this.scanner = new Scanner(System.in);
    }


    public void displayAllCurrencies() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            // Fetch currency data from the REST API
            Map<String, List<Map<String, Object>>> currencies = restTemplate.getForObject(baseUrl + "/currencies", Map.class);

            // Display currency data
            if (currencies != null) {
                // Sort the currencies by region alphabetically
                List<Map.Entry<String, List<Map<String, Object>>>> sortedCurrencies = currencies.entrySet().stream()
                        .sorted(Comparator.comparing(Map.Entry::getKey))
                        .collect(Collectors.toList());

                // Print the sorted currency data
                for (Map.Entry<String, List<Map<String, Object>>> entry : sortedCurrencies) {
                    System.out.println("Currencies in " + entry.getKey() + ":");
                    for (Map<String, Object> currencyInfo : entry.getValue()) {
                        String name = (String) currencyInfo.get("name");
                        if (!"Unknown Currency".equals(name)) {
                            System.out.println("Currency Code: " + currencyInfo.get("code"));
                            System.out.println("Name: " + name);
                            System.out.println("Value: " + currencyInfo.get("value"));
                            System.out.println();
                        }
                    }
                }
            } else {
                System.out.println("No currency data available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Initialize CurrencyService with the base URL of the REST API
        CurrencyService currencyService = new CurrencyService("http://localhost:8080");

        // Display all currencies
        currencyService.displayAllCurrencies();
    }

    public void displayCurrenciesByRegion(String region) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            // Fetch currency data from the REST API
            Map<String, List<Map<String, Object>>> currencies = restTemplate.getForObject(baseUrl + "/currencies", Map.class);

            // Display currency data for the specified region
            if (currencies != null) {
                System.out.println("Currencies in " + region + ":");

                // Create a list to store currency info
                List<Map<String, Object>> currencyList = currencies.values().stream()
                        .flatMap(List::stream)
                        .filter(currencyInfo -> region.equals(currencyInfo.get("region")))
                        .sorted(Comparator.comparing(currencyInfo -> (String) currencyInfo.get("name")))
                        .collect(Collectors.toList());

                // Display sorted currency data
                for (Map<String, Object> currencyInfo : currencyList) {
                    System.out.println("Currency Code: " + currencyInfo.get("code"));
                    System.out.println("Name: " + currencyInfo.get("name"));
                    System.out.println("Value: " + currencyInfo.get("value"));
                    System.out.println();
                }
            } else {
                System.out.println("No currency data available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAmountToWithdraw(double amount) {
        this.amountToWithdraw = amount;
    }

    public double getAmountToWithdraw() {
        return amountToWithdraw;
    }

    String codeForExchange;

    public String CurrencyDetails() {
        RestTemplate restTemplate = new RestTemplate();
        Scanner scanner = new Scanner(System.in);

        try {
            // Fetch currency data from the REST API
            Map<String, List<Map<String, Object>>> currencies = restTemplate.getForObject(baseUrl + "/currencies", Map.class);

            // Display currency data
            if (currencies != null) {
                // Sort the currencies by region alphabetically
                List<Map.Entry<String, List<Map<String, Object>>>> sortedCurrencies = currencies.entrySet().stream()
                        .sorted(Comparator.comparing(Map.Entry::getKey))
                        .collect(Collectors.toList());

                // Prompt the user to select a currency code
                System.out.println();
                System.out.println("Please enter a currency code to display details for Exchange:");
                String currencyCode = scanner.nextLine().toUpperCase(); // Convert input to uppercase for consistency
                codeForExchange = currencyCode;

                // Check if the entered currency code exists in the map
                boolean found = false;
                for (List<Map<String, Object>> currencyList : currencies.values()) {
                    for (Map<String, Object> currencyInfo : currencyList) {
                        if (currencyInfo.get("code").equals(currencyCode)) {
                            found = true;
                            System.out.println("Details for currency code " + currencyCode + ":");
                            System.out.println("Name: " + currencyInfo.get("name"));
                            System.out.println("Value: " + currencyInfo.get("value"));
                            System.out.println();
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Currency code not found.");

                }
            } else {
                System.out.println("No currency data available.");
            }
            makeExchange();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codeForExchange;
    }

    public double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(double feeAmount) {
        this.feeAmount = feeAmount;
    }

    private double feeAmount;
    private double amountToWithdraw;

    private List<String> convertedAmountTranscript = new ArrayList<>();

    public String getConvertedAmountTranscript() {
        if (convertedAmountTranscript.isEmpty()) {
            return "No Exchange";
        } else {
            // Join all entries with a newline character
            return String.join("\n", convertedAmountTranscript);
        }
    }

    public double[] makeExchange() {
        RestTemplate restTemplate = new RestTemplate();
        Scanner scanner = new Scanner(System.in);
        try {
            // Fetch currency data from the REST API
            Map<String, List<Map<String, Object>>> currencies = restTemplate.getForObject(baseUrl + "/currencies", Map.class);

            // Prompt the user to enter the amount to convert
            double amountUSD = 0;
            while (true) {
                System.out.println("Please enter the amount in USD to convert:");
                String input = scanner.nextLine();
                try {
                    amountUSD = Double.parseDouble(input);
                    if (amountUSD < 0) {
                        System.out.println("Amount must be non-negative. Please try again.");
                    } else {
                        break; // valid input
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid number.");
                }
            }

            // Check if the entered currency code exists in the map
            boolean found = false;
            for (List<Map<String, Object>> currencyList : currencies.values()) {
                for (Map<String, Object> currencyInfo : currencyList) {
                    if (currencyInfo.get("code").equals(codeForExchange)) {
                        found = true;

                        // Perform the conversion
                        double currencyValue = (double) currencyInfo.get("value");
                        String currencyName = (String) currencyInfo.get("name");

                        // Calculate the fee (3%)
                        double fee = amountUSD * 0.03;

                        // Subtract the fee from the original amount
                        double amountAfterFee = amountUSD - fee;

                        // Perform the conversion with the amount minus the fee
                        double convertedAmount = amountAfterFee * currencyValue;

                        // Round the conversion result to two decimal points
                        String roundedConvertedAmount = String.format("%.2f", convertedAmount);

                        // Display the conversion result including the fee
                        System.out.println();
                        System.out.println(amountUSD + " USD will be exchanged to " + roundedConvertedAmount + " " + codeForExchange + " including a 3% fee.");
                        System.out.println();

                        // Ask for confirmation
                        System.out.println("Do you want to proceed with the conversion? (yes/no)");
                        String confirmation = scanner.nextLine().toLowerCase();
                        if (!confirmation.equals("yes")) {
                            System.out.println("Conversion canceled.");
                            return new double[]{0, 0, 0}; // Return zeros to indicate cancellation
                        }

                        // Store the amount to withdraw and the fee
                        String transactionDetails = "Currency Name: " + currencyName + ", Converted Amount: " + roundedConvertedAmount;
                        convertedAmountTranscript.add(transactionDetails);
                        feeAmount = fee;
                        amountToWithdraw = amountUSD;

                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
            if (!found) {
                System.out.println("Invalid currency code.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Return only the numeric values
        return new double[]{amountToWithdraw, feeAmount, 0}; // 0 for convertedAmountTranscript as it's now a list of strings
    }


    public double displayCurrencyDetails() {
        RestTemplate restTemplate = new RestTemplate();
        double amountUSD = 0;
        Scanner scanner = new Scanner(System.in);

        try {
            // Fetch currency data from the REST API
            Map<String, List<Map<String, Object>>> currencies = restTemplate.getForObject(baseUrl + "/currencies", Map.class);

            // Display currency data
            if (currencies != null) {
                // Sort the currencies by region alphabetically
                List<Map.Entry<String, List<Map<String, Object>>>> sortedCurrencies = currencies.entrySet().stream()
                        .sorted(Comparator.comparing(Map.Entry::getKey))
                        .collect(Collectors.toList());

                // Prompt the user to select a currency code
                System.out.println("Please enter a currency code to display details & convert:");
                String currencyCode = scanner.nextLine().toUpperCase(); // Convert input to uppercase for consistency


                // Check if the entered currency code exists in the map
                boolean found = false;
                for (List<Map<String, Object>> currencyList : currencies.values()) {
                    for (Map<String, Object> currencyInfo : currencyList) {
                        if (currencyInfo.get("code").equals(currencyCode)) {
                            found = true;
                            System.out.println("Details for currency code " + currencyCode + ":");
                            System.out.println("Name: " + currencyInfo.get("name"));
                            double currencyValue = (double) currencyInfo.get("value");
                            System.out.println("Value: " + currencyValue);
                            System.out.println();

                            // Perform the conversion
                            double convertedAmount = amountUSD * currencyValue;

                            // Round the conversion result to two decimal points
                            String roundedConvertedAmount = String.format("%.2f", convertedAmount);

                            // Display the conversion result
                            System.out.println(amountUSD + " USD is equivalent to " + roundedConvertedAmount + " " + currencyCode);

                            // Store the amount to withdraw
                            setAmountToWithdraw(convertedAmount);

                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
                if (!found) {
                    System.out.println("Invalid currency code.");
                }
            } else {
                System.out.println("No currency data available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      return 0;
    }
}
