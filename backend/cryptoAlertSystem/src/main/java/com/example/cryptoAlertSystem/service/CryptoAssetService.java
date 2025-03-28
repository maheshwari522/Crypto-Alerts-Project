package com.example.cryptoAlertSystem.service;

import com.example.cryptoAlertSystem.model.CryptoAsset;
import com.example.cryptoAlertSystem.repository.CryptoAssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CryptoAssetService {

    private final CryptoAssetRepository repository;

    public CryptoAssetService(CryptoAssetRepository repository){
        this.repository = repository;
    }

    public List<CryptoAsset> getAllAssets(){
        return repository.findAll();
    }

    public CryptoAsset addAsset(CryptoAsset asset){
        return repository.save(asset);
    }

}
