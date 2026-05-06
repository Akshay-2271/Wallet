package com.wallet.app.controller;

import com.wallet.app.dto.WalletRequest;
import com.wallet.app.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallet")
    public ResponseEntity<String> updateWallet(
            @Valid @RequestBody WalletRequest request) {

        walletService.processWallet(request);

        return ResponseEntity.ok("Wallet updated successfully");
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable String walletId) {

        return ResponseEntity.ok(
                walletService.getBalance(walletId)
        );
    }
}