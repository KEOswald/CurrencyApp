package com.techelevator.tenmo.services;

import java.util.*;

import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CurrencyService {

    private final String baseUrl;

    public CurrencyService(String baseUrl) {
        this.baseUrl = baseUrl;
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
}

