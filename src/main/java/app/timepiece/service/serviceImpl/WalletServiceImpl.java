package app.timepiece.service.serviceImpl;


import app.timepiece.entity.Wallet;
import app.timepiece.repository.WalletRepository;
import app.timepiece.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet depositToWallet(Long walletId, Double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));
        try {
            wallet.setBalance(wallet.getBalance() + amount);
            return walletRepository.save(wallet);
        } catch (Exception e) {
            // Handle other potential exceptions during the deposit process
            throw new RuntimeException("Failed to deposit amount to wallet", e);
        }
    }

    @Override
    public boolean withdrawFromWallet(Long walletId, Double amount) {
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new RuntimeException("Wallet not found"));
        if (wallet.getBalance() >= amount) {
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Wallet> getWalletByUserId(Long userId) {
        return walletRepository.findByUserId(userId);
    }
}
