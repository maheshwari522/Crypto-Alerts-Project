package com.example.cryptoAlertSystem.repository;

import com.example.cryptoAlertSystem.model.CryptoAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoAssetRepository extends JpaRepository<CryptoAsset, Long> {
    // No code needed — Spring auto-implements!
}
