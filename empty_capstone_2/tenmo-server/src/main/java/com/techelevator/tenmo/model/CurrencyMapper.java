package com.techelevator.tenmo.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.techelevator.tenmo.model.CurrencyInfo;

import static com.techelevator.tenmo.model.CurrencyInfo.determineRegion;

//Class to hold Currency Data Map and Print to Console
public class CurrencyMapper {

    //Method to create Currency Hash Map
    public static Map<String, CurrencyInfo> createCurrencyMap(JSONObject jsonResponse) {
        Map<String, CurrencyInfo> currencyMap = new HashMap<>();
        JSONObject rates = jsonResponse.getJSONObject("rates");
        for (String currencyCode : rates.keySet()) {
            String currencyName = getCurrencyName(currencyCode);
            double currencyValue = rates.getDouble(currencyCode);
            String region = determineRegion(currencyCode); // Method to determine region based on currency code
            currencyMap.put(currencyCode, new CurrencyInfo(currencyCode, currencyName, currencyValue, region));
        }
        return currencyMap;
    }

    //Method to print Currency Data to the console
    public static void printCurrencyData(JSONObject currencyData) {
        Map<String, List<CurrencyInfo>> currencyMapByRegion = new HashMap<>();
        Map<String, CurrencyInfo> currencyMap = createCurrencyMap(currencyData);

        // Group currencies by region
        for (CurrencyInfo currencyInfo : currencyMap.values()) {
            String region = currencyInfo.getRegion();
            if (!currencyMapByRegion.containsKey(region)) {
                currencyMapByRegion.put(region, new ArrayList<>());
            }
            currencyMapByRegion.get(region).add(currencyInfo);
        }

        // Print currencies by region
        for (String region : currencyMapByRegion.keySet()) {
            System.out.println("Region: " + region);
            for (CurrencyInfo currencyInfo : currencyMapByRegion.get(region)) {
                // Skip printing unknown currencies
                if (!currencyInfo.getName().equals("Unknown Currency")) {
                    System.out.println(currencyInfo.getCode() + " \"" + currencyInfo.getName() + "\": " + currencyInfo.getValue());
                }
            }
            System.out.println(); // Add a blank line between regions
        }
    }

    public static Map<String, List<CurrencyInfo>> getCurrencyData(JSONObject currencyData) {
        Map<String, List<CurrencyInfo>> currencyMapByRegion = new HashMap<>();
        Map<String, CurrencyInfo> currencyMap = createCurrencyMap(currencyData);

        // Group currencies by region
        for (CurrencyInfo currencyInfo : currencyMap.values()) {
            String region = currencyInfo.getRegion();
            if (!currencyMapByRegion.containsKey(region)) {
                currencyMapByRegion.put(region, new ArrayList<>());
            }
            currencyMapByRegion.get(region).add(currencyInfo);
        }
        return currencyMapByRegion;

    }

    //Method to get the Currency Name
    //Resolves API double print issue with printCurrencyData method
    private static String getCurrencyName(String currencyCode) {
        // Look up the currency name from the mapping
        return CURRENCY_NAMES.getOrDefault(currencyCode, "Unknown Currency");
    }

    public static Map<String, String> getCurrenciesByRegion(JSONObject currencyData) {
        Map<String, String> currenciesByRegion = new HashMap<>();
        Map<String, CurrencyInfo> currencyMap = createCurrencyMap(currencyData);

        // Group currencies by region
        for (CurrencyInfo currencyInfo : currencyMap.values()) {
            String region = currencyInfo.getRegion();
            String currencyInfoString = currencyInfo.getCode() + " \"" + currencyInfo.getName() + "\": " + currencyInfo.getValue();
            currenciesByRegion.merge(region, currencyInfoString, (existingValue, newValue) -> existingValue + ", " + newValue);
        }

        return currenciesByRegion;
    }



    // Define a mapping of currency codes to their full names
    private static final Map<String, String> CURRENCY_NAMES = new HashMap<>();
    static {
        // Populate the map with currency mappings
        CURRENCY_NAMES.put("FJD", "Fijian Dollar");
        CURRENCY_NAMES.put("MXN", "Mexican Peso");
        CURRENCY_NAMES.put("STD", "São Tomé and Príncipe Dobra (pre-2018)");
        CURRENCY_NAMES.put("SCR", "Seychellois Rupee");
        CURRENCY_NAMES.put("CDF", "Congolese Franc");
        CURRENCY_NAMES.put("BBD", "Barbadian Dollar");
        CURRENCY_NAMES.put("GTQ", "Guatemalan Quetzal");
        CURRENCY_NAMES.put("CLP", "Chilean Peso");
        CURRENCY_NAMES.put("HNL", "Honduran Lempira");
        CURRENCY_NAMES.put("UGX", "Ugandan Shilling");
        CURRENCY_NAMES.put("ZAR", "South African Rand");
        CURRENCY_NAMES.put("TND", "Tunisian Dinar");
        CURRENCY_NAMES.put("STN", "São Tomé and Príncipe Dobra");
        CURRENCY_NAMES.put("CUC", "Cuban Convertible Peso");
        CURRENCY_NAMES.put("BSD", "Bahamian Dollar");
        CURRENCY_NAMES.put("SLL", "Sierra Leonean Leone");
        CURRENCY_NAMES.put("SDG", "Sudanese Pound");
        CURRENCY_NAMES.put("IQD", "Iraqi Dinar");
        CURRENCY_NAMES.put("CUP", "Cuban Peso");
        CURRENCY_NAMES.put("GMD", "Gambian Dalasi");
        CURRENCY_NAMES.put("TWD", "New Taiwan Dollar");
        CURRENCY_NAMES.put("RSD", "Serbian Dinar");
        CURRENCY_NAMES.put("DOP", "Dominican Peso");
        CURRENCY_NAMES.put("KMF", "Comorian Franc");
        CURRENCY_NAMES.put("MYR", "Malaysian Ringgit");
        CURRENCY_NAMES.put("FKP", "Falkland Islands Pound");
        CURRENCY_NAMES.put("XOF", "West African CFA franc");
        CURRENCY_NAMES.put("GEL", "Georgian Lari");
        CURRENCY_NAMES.put("BTC", "Bitcoin");
        CURRENCY_NAMES.put("UYU", "Uruguayan Peso");
        CURRENCY_NAMES.put("MAD", "Moroccan Dirham");
        CURRENCY_NAMES.put("CVE", "Cape Verdean Escudo");
        CURRENCY_NAMES.put("TOP", "Tongan Pa'anga");
        CURRENCY_NAMES.put("AZN", "Azerbaijani Manat");
        CURRENCY_NAMES.put("OMR", "Omani Rial");
        CURRENCY_NAMES.put("PGK", "Papua New Guinean Kina");
        CURRENCY_NAMES.put("KES", "Kenyan Shilling");
        CURRENCY_NAMES.put("SEK", "Swedish Krona");
        CURRENCY_NAMES.put("CNH", "Chinese Yuan (offshore)");
        CURRENCY_NAMES.put("BTN", "Bhutanese Ngultrum");
        CURRENCY_NAMES.put("UAH", "Ukrainian Hryvnia");
        CURRENCY_NAMES.put("GNF", "Guinean Franc");
        CURRENCY_NAMES.put("ERN", "Eritrean Nakfa");
        CURRENCY_NAMES.put("MZN", "Mozambican Metical");
        CURRENCY_NAMES.put("SVC", "Salvadoran Colón");
        CURRENCY_NAMES.put("ARS", "Argentine Peso");
        CURRENCY_NAMES.put("QAR", "Qatari Riyal");
        CURRENCY_NAMES.put("IRR", "Iranian Rial");
        CURRENCY_NAMES.put("XPD", "Palladium Ounce");
        CURRENCY_NAMES.put("CNY", "Chinese Yuan");
        CURRENCY_NAMES.put("THB", "Thai Baht");
        CURRENCY_NAMES.put("UZS", "Uzbekistani Som");
        CURRENCY_NAMES.put("XPF", "CFP Franc");
        CURRENCY_NAMES.put("MRU", "Mauritanian Ouguiya");
        CURRENCY_NAMES.put("BDT", "Bangladeshi Taka");
        CURRENCY_NAMES.put("LYD", "Libyan Dinar");
        CURRENCY_NAMES.put("BMD", "Bermudian Dollar");
        CURRENCY_NAMES.put("KWD", "Kuwaiti Dinar");
        CURRENCY_NAMES.put("PHP", "Philippine Peso");
        CURRENCY_NAMES.put("XPT", "Platinum Ounce");
        CURRENCY_NAMES.put("RUB", "Russian Ruble");
        CURRENCY_NAMES.put("PYG", "Paraguayan Guarani");
        CURRENCY_NAMES.put("ISK", "Icelandic Króna");
        CURRENCY_NAMES.put("JMD", "Jamaican Dollar");
        CURRENCY_NAMES.put("COP", "Colombian Peso");
        CURRENCY_NAMES.put("MKD", "Macedonian Denar");
        CURRENCY_NAMES.put("USD", "United States Dollar");
        CURRENCY_NAMES.put("DZD", "Algerian Dinar");
        CURRENCY_NAMES.put("PAB", "Panamanian Balboa");
        CURRENCY_NAMES.put("GGP", "Guernsey Pound");
        CURRENCY_NAMES.put("SGD", "Singapore Dollar");
        CURRENCY_NAMES.put("ETB", "Ethiopian Birr");
        CURRENCY_NAMES.put("JEP", "Jersey Pound");
        CURRENCY_NAMES.put("KGS", "Kyrgyzstani Som");
        CURRENCY_NAMES.put("SOS", "Somali Shilling");
        CURRENCY_NAMES.put("VUV", "Vanuatu Vatu");
        CURRENCY_NAMES.put("LAK", "Laotian Kip");
        CURRENCY_NAMES.put("BND", "Brunei Dollar");
        CURRENCY_NAMES.put("XAF", "Central African CFA Franc");
        CURRENCY_NAMES.put("LRD", "Liberian Dollar");
        CURRENCY_NAMES.put("XAG", "Silver Ounce");
        CURRENCY_NAMES.put("CHF", "Swiss Franc");
        CURRENCY_NAMES.put("HRK", "Croatian Kuna");
        CURRENCY_NAMES.put("ALL", "Albanian Lek");
        CURRENCY_NAMES.put("DJF", "Djiboutian Franc");
        CURRENCY_NAMES.put("VES", "Venezuelan Bolívar");
        CURRENCY_NAMES.put("ZMW", "Zambian Kwacha");
        CURRENCY_NAMES.put("TZS", "Tanzanian Shilling");
        CURRENCY_NAMES.put("VND", "Vietnamese Dong");
        CURRENCY_NAMES.put("XAU", "Gold Ounce");
        CURRENCY_NAMES.put("AUD", "Australian Dollar");
        CURRENCY_NAMES.put("ILS", "Israeli New Shekel");
        CURRENCY_NAMES.put("GHS", "Ghanaian Cedi");
        CURRENCY_NAMES.put("GYD", "Guyanaese Dollar");
        CURRENCY_NAMES.put("KPW", "North Korean Won");
        CURRENCY_NAMES.put("BOB", "Bolivian Boliviano");
        CURRENCY_NAMES.put("KHR", "Cambodian Riel");
        CURRENCY_NAMES.put("MDL", "Moldovan Leu");
        CURRENCY_NAMES.put("IDR", "Indonesian Rupiah");
        CURRENCY_NAMES.put("KYD", "Cayman Islands Dollar");
        CURRENCY_NAMES.put("AMD", "Armenian Dram");
        CURRENCY_NAMES.put("BWP", "Botswanan Pula");
        CURRENCY_NAMES.put("SHP", "Saint Helena Pound");
        // Add more mappings for other currencies as needed
    }

}
