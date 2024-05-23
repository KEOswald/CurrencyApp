package com.techelevator.tenmo.services;

import java.util.*;

import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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

    public void displayCurrencyDetails() {
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

                // Prompt the user to select a currency code
                System.out.println("Please enter a currency code to display details:");
                Scanner scanner = new Scanner(System.in);
                String currencyCode = scanner.nextLine().toUpperCase(); // Convert input to uppercase for consistency

                // Prompt the user to enter the amount to convert
                System.out.println("Please enter the amount in USD to convert:");
                double amountUSD = scanner.nextDouble();

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
    }

    private double getConversionRate(String currencyCode) {
        // You may hardcode conversion rates if needed or fetch from an API
        // For simplicity, you can directly return 1.0 since USD is the base currency
        return 1.0;
    }
}


