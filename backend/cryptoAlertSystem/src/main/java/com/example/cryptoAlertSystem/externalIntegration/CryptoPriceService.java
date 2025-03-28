package com.example.cryptoAlertSystem.externalIntegration;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class CryptoPriceService {


    private static final String COINGECKO_API = "https://api.coingecko.com/api/v3/simple/price?ids=%s&vs_currencies=usd";

    private final RestTemplate restTemplate = new RestTemplate();

    public double getPrice(String symbol){
        String id = mapSymbolToCoinGeckoId(symbol);
        String url = String.format(COINGECKO_API,id);

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Integer>> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey(id)) {
            return response.get(id).get("usd");
        }

        throw new RuntimeException("Price not found for symbol: " + symbol);
    }

    private String mapSymbolToCoinGeckoId(String symbol) {
        return switch (symbol.toUpperCase()) {
            case "BTC" -> "bitcoin";
            case "ETH" -> "ethereum";
            case "DOGE" -> "dogecoin";
            default -> symbol.toLowerCase(); // fallback
        };
    }


}
