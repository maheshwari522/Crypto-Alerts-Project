package com.example.cryptoAlertSystem.controller;

import com.example.cryptoAlertSystem.externalIntegration.CryptoPriceService;
import com.example.cryptoAlertSystem.model.CryptoAsset;
import com.example.cryptoAlertSystem.service.CryptoAssetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/assets")
public class CryptoAssetsController {

    private final CryptoAssetService service;
    private final CryptoPriceService priceService;


    public CryptoAssetsController(CryptoAssetService service, CryptoPriceService priceService){
        this.service = service;
        System.out.println("🚀 CryptoAssetController initialized");
        this.priceService = priceService;
    }

    @GetMapping
    public List<CryptoAsset> getAllAssets() {
        return service.getAllAssets();
    }

    @PostMapping
    public CryptoAsset addAsset(@RequestBody CryptoAsset asset) {
        return service.addAsset(asset);
    }

    @GetMapping("/price/{symbol}")
    public Map<String, Object> getLivePrice(@PathVariable String symbol) {
        double price = priceService.getPrice(symbol);
        return Map.of("symbol", symbol.toUpperCase(), "priceUsd", price);
    }






}
