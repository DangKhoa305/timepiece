package app.timepiece.service.serviceImpl;

import app.timepiece.dto.RegistrationRequestDTO;
import app.timepiece.entity.Account;
import app.timepiece.entity.Role;
import app.timepiece.entity.User;
import app.timepiece.repository.RoleRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.AccountService;
import app.timepiece.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<User> loginUser(String email, String password) {
        Account account = accountRepository.findByEmailAndPassword(email, password);
        if (account != null) {
            return Optional.ofNullable(account.getUser());
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public Optional<User> registerUser(RegistrationRequestDTO registrationRequest) {
        // Kiểm tra xem email đã được sử dụng chưa
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Kiểm tra xác nhận mật khẩu
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Tạo tài khoản mới
        Account account = new Account();
        account.setEmail(registrationRequest.getEmail());
        account.setPassword(registrationRequest.getPassword());

        // Tạo thông tin người dùng mới
        User user = new User();
        user.setName(registrationRequest.getName());
        user.setPhoneNumber(registrationRequest.getPhoneNumber());
        user.setGender(registrationRequest.getGender());
        user.setBirthday(registrationRequest.getBirthday());

        // Thiết lập roleId thành 1
        Role defaultRole = roleRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
        user.setRole(defaultRole);

        // Liên kết tài khoản và người dùng
        user.setAccount(account);
        account.setUser(user);

        // Sau đó lưu tài khoản
        accountRepository.save(account);

        // Lưu thông tin người dùng trước
        userRepository.save(user);



        return Optional.of(user);
    }
}


