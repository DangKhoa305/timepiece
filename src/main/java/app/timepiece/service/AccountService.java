package app.timepiece.service;

import app.timepiece.entity.Account;

import java.util.Optional;


public interface AccountService {
        Account saveAccount(Account account);
        Optional<Account> findByEmail(String email);
}

