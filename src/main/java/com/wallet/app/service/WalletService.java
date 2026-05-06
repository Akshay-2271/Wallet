package com.wallet.app.service;

import com.wallet.app.dto.WalletRequest;
import com.wallet.app.entity.Wallet;
import com.wallet.app.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public void processWallet(WalletRequest request) {

        Wallet wallet = walletRepository.findById(request.getWalletId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if ("DEPOSIT".equalsIgnoreCase(request.getOperationType())) {

            wallet.setBalance(
                    wallet.getBalance().add(request.getAmount())
            );

        } else if ("WITHDRAW".equalsIgnoreCase(request.getOperationType())) {

            if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Insufficient balance");
            }

            wallet.setBalance(
                    wallet.getBalance().subtract(request.getAmount())
            );
        }

        walletRepository.save(wallet);
    }

    public BigDecimal getBalance(String walletId) {

        Wallet wallet = walletRepository.findById(java.util.UUID.fromString(walletId))
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        return wallet.getBalance();
    }
}