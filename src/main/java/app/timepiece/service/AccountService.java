package app.timepiece.service;

import app.timepiece.entity.Account;


public interface AccountService {
        Account saveAccount(Account account);
        Account findByEmail(String email);
}

