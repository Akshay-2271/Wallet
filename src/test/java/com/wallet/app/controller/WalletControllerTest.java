
package com.wallet.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.app.dto.WalletRequest;
import com.wallet.app.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldUpdateWalletSuccessfully() throws Exception {

        WalletRequest request = new WalletRequest();

        request.setWalletId(UUID.randomUUID());
        request.setOperationType("CREDIT");
        request.setAmount(BigDecimal.valueOf(500));

        doNothing().when(walletService)
                .processWallet(any(WalletRequest.class));

        mockMvc.perform(
                        post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Wallet updated successfully"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidRequest() throws Exception {

        WalletRequest request = new WalletRequest();

        request.setOperationType("CREDIT");

        mockMvc.perform(
                        post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnWalletBalanceSuccessfully() throws Exception {

        String walletId =
                "11111111-1111-1111-1111-111111111111";

        when(walletService.getBalance(walletId))
                .thenReturn(BigDecimal.valueOf(1500));

        mockMvc.perform(
                        get("/api/v1/wallets/{walletId}", walletId)
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1500"));
    }

    @Test
    void shouldThrowExceptionWhenWalletNotFound() throws Exception {

        String walletId =
                "11111111-1111-1111-1111-111111111111";

        when(walletService.getBalance(walletId))
                .thenThrow(new RuntimeException("Wallet not found"));

        mockMvc.perform(
                        get("/api/v1/wallets/{walletId}", walletId)
                )
                .andExpect(status().is5xxServerError());
    }
}
