package app.timepiece.service;

import app.timepiece.entity.Wallet;

import java.util.Optional;

public interface WalletService {

    Wallet depositToWallet(Long walletId, Double amount);

    boolean withdrawFromWallet(Long walletId, Double amount);

    Optional<Wallet> getWalletByUserId(Long userId);
}
