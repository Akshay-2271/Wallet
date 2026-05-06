package com.wallet.app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class WalletRequest {

    @NotNull
    private UUID walletId;

    @NotNull
    private String operationType;

    @Positive
    private BigDecimal amount;
}