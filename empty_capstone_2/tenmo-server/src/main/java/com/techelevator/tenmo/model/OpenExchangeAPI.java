package com.techelevator.tenmo.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class OpenExchangeAPI {
    //Open Exchange API information
    private static final String API_BASE_URL = "https://openexchangerates.org/api/";
    private static final String LATEST_ENDPOINT = "latest.json";
    private static final String API_KEY = "5629b904a3d64d0f97fe50ca2c9117af";

    public static String getApiBaseUrl() {
        return API_BASE_URL;
    }

    public static String getLatestEndpoint() {
        return LATEST_ENDPOINT;
    }

    public static String getApiKey() {
        return API_KEY;
    }
    public static void main(String[] args) {
        try {
            // Fetch currency data
            JSONObject currencyData = fetchCurrencyData();

            // Print currency data
            System.out.println("Currency Code, Name, and Current Value:");
            CurrencyMapper.printCurrencyData(currencyData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Method to retrieve Currency Data from Open Exchange API
    public static JSONObject fetchCurrencyData() throws Exception {
        String apiUrl = API_BASE_URL + LATEST_ENDPOINT + "?app_id=" + API_KEY;
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse the JSON response
        return new JSONObject(response.toString());
    }
}