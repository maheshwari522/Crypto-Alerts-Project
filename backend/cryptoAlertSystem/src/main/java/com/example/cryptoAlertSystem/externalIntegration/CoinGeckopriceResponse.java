package com.example.cryptoAlertSystem.externalIntegration;

import java.util.Map;

public class CoinGeckopriceResponse {

    private class CoinGeckoPriceResponse {
        private Map<String, Map<String,Integer>> prices;

        public Map<String, Map<String,Integer>> getPrices(){
            return prices;
        }

        public void setPrices(Map<String, Map<String, Integer>> prices) {
            this.prices = prices;
        }
    }
}
