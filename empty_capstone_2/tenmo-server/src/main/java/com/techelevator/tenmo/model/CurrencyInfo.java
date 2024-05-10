package com.techelevator.tenmo.model;

public class CurrencyInfo {
    private String code;

    public CurrencyInfo(String code, String name, double value, String region) {
        this.code = code;
        this.name = name;
        this.value = value;
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    private String name;
    private double value;
    private String region;

    public String getCode() {
        return code;
    }

    public static String determineRegion(String currencyCode) {
        String region;
        switch (currencyCode) {
            case "EUR":
            case "GBP":
            case "CHF":
            case "SEK":
            case "NOK":
            case "DKK":
            case "PLN":
            case "RUB":
            case "ALL":
            case "HRK":
            case "ISK":
            case "MKD":
            case "MDL":
            case "RON":
            case "RSD":
            case "TRY":
            case "UAH":
                return "Europe";

            // North America
            case "USD":
            case "CAD":
            case "MXN":
                return "North America";

            // Asia
            case "JPY":
            case "CNY":
            case "KRW":
            case "INR":
            case "SGD":
            case "HKD":
            case "THB":
            case "TWD":
            case "BDT":
            case "BTN":
            case "KHR":
            case "KGS":
            case "LAK":
            case "MOP":
            case "MVR":
            case "MNT":
            case "MMK":
            case "NPR":
            case "PKR":
            case "LKR":
            case "TJS":
            case "UZS":
            case "MYR":
            case "GEL":
            case "AZN":
            case "PHP":
            case "IDR":
                return "Asia";

            // Oceania
            case "AUD":
            case "NZD":
            case "FJD":
            case "PGK":
            case "SBD":
            case "TOP":
            case "VUV":
                return "Oceania";

            // Africa
            case "ZAR":
            case "NGN":
            case "EGP":
            case "KES":
            case "DZD":
            case "AOA":
            case "XOF":
            case "XAF":
            case "BIF":
            case "CVE":
            case "DJF":
            case "GMD":
            case "GNF":
            case "KMF":
            case "LRD":
            case "LSL":
            case "MGA":
            case "MRO":
            case "MUR":
            case "MWK":
            case "NAD":
            case "RWF":
            case "SAR":
            case "SCR":
            case "SHP":
            case "SLL":
            case "SOS":
            case "STD":
            case "SZL":
            case "TND":
            case "TZS":
            case "UGX":
            case "ZMW":
            case "CDF":
            case "STN":
            case "SDG":

                return "Africa";

            // South America
            case "BRL":
            case "ARS":
            case "CLP":
            case "COP":
            case "BOB":
            case "PYG":
            case "UYU":
            case "VEF":
            case "SRD":
            case "GUY":
            case "FKP":
            case "VES":
                return "South America";

            // Caribbean
            case "TTD":
            case "JMD":
            case "XCD":
            case "BDD":
            case "CUC":
            case "BSD":
            case"CUP":
            case "DOP":
                return "Caribbean";

            // Middle East
            case "AED":
            case "BHD":
            case "IQD":
            case "IRR":
            case "ILS":
            case "JOD":
            case "KWD":
            case "LBP":
            case "LYD":
            case "MAD":
            case "OMR":
            case "QAR":
            case "SYP":
                return "Middle East";

            // Central America
            case "CRC":
            case "GTQ":
            case "HNL":
            case "NIO":
                return "Central America";

            case "BTC":
            case "XAG":
            case "XAU":
            case "XPT":
                return "Crypto & Commodity";

            // Default case
            default:
                return "Unknown";
        }
    }
}
