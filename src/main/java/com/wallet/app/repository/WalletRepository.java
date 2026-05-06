package com.wallet.app.repository;

import com.wallet.app.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface WalletRepository extends JpaRepository<Wallet, UUID>{

}


