
package com.wallet.app.service;

import com.wallet.app.dto.WalletRequest;
import com.wallet.app.entity.Wallet;
import com.wallet.app.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private Wallet wallet;
    private WalletRequest request;
    private UUID walletId;

    @BeforeEach
    void setUp() {

        walletId = UUID.randomUUID();

        wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(BigDecimal.valueOf(1000));

        request = new WalletRequest();
        request.setWalletId(walletId);
    }

    @Test
    void shouldCreditAmountSuccessfully() {

        request.setOperationType("CREDIT");
        request.setAmount(BigDecimal.valueOf(500));

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.of(wallet));

        walletService.processWallet(request);

        assertEquals(
                BigDecimal.valueOf(1500),
                wallet.getBalance()
        );

        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void shouldDebitAmountSuccessfully() {

        request.setOperationType("DEBIT");
        request.setAmount(BigDecimal.valueOf(300));

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.of(wallet));

        walletService.processWallet(request);

        assertEquals(
                BigDecimal.valueOf(700),
                wallet.getBalance()
        );

        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {

        request.setOperationType("DEBIT");
        request.setAmount(BigDecimal.valueOf(2000));

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.of(wallet));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> walletService.processWallet(request)
        );

        assertEquals(
                "Insufficient balance",
                exception.getMessage()
        );

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() {

        request.setOperationType("CREDIT");
        request.setAmount(BigDecimal.valueOf(100));

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> walletService.processWallet(request)
        );

        assertEquals(
                "Wallet not found",
                exception.getMessage()
        );

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionForInvalidOperationType() {

        request.setOperationType("TEST");
        request.setAmount(BigDecimal.valueOf(100));

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.of(wallet));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> walletService.processWallet(request)
        );

        assertEquals(
                "Invalid operation type",
                exception.getMessage()
        );

        verify(walletRepository, never()).save(any());
    }

    @Test
    void shouldReturnBalanceSuccessfully() {

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.of(wallet));

        BigDecimal balance =
                walletService.getBalance(walletId.toString());

        assertEquals(
                BigDecimal.valueOf(1000),
                balance
        );
    }

    @Test
    void shouldThrowExceptionWhenGettingBalanceForInvalidWallet() {

        when(walletRepository.findById(walletId))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> walletService.getBalance(walletId.toString())
        );

        assertEquals(
                "Wallet not found",
                exception.getMessage()
        );
    }
}
