package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

    private static final String BASE_URL = "http://localhost:8080/api/currencies";

    private AuthenticatedUser currentUser;

    public CurrencyService(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public JSONObject fetchCurrencyData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> response = restTemplate.getForEntity(BASE_URL + "/available", JSONObject.class);
        return response.getBody();
    }
    public static void main(String[] args) {
        CurrencyService currencyService = new CurrencyService();
        JSONObject currencyData = currencyService.fetchCurrencyData();
        System.out.println("Currency Data: " + currencyData);
    }
    public CurrencyService() {
        // No initialization needed
    }
}

