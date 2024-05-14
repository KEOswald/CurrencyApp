package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.CurrencyInfo;
import com.techelevator.tenmo.model.CurrencyMapper;
import com.techelevator.tenmo.model.OpenExchangeAPI;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CurrencyController {

    @RequestMapping(path="/currencies", method = RequestMethod.GET)
    public Map<String, List<CurrencyInfo>> getCurrencies() {
        try {
            // Fetch currency data
            JSONObject currencyData = OpenExchangeAPI.fetchCurrencyData();

            // Print currency data
            System.out.println("Currency Code, Name, and Current Value:");
            return CurrencyMapper.getCurrencyData(currencyData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }








}
