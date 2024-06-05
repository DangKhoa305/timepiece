package app.timepiece.service.serviceImpl;

import app.timepiece.entity.Account;
import app.timepiece.repository.AccountRepository;
import app.timepiece.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account saveAccount(Account account) {
        account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
        return accountRepository.save(account);
    }

    @Override
    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}
