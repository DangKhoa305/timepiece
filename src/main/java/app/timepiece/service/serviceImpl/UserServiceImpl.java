package app.timepiece.service.serviceImpl;

import app.timepiece.dto.RegistrationRequestDTO;
import app.timepiece.entity.Account;
import app.timepiece.entity.Role;
import app.timepiece.entity.User;
import app.timepiece.repository.AccountRepository;
import app.timepiece.repository.RoleRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @Override
    public Optional<User> registerUser(RegistrationRequestDTO registrationRequest) {
        // Kiểm tra xem email đã được sử dụng chưa
        if (accountRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        // Kiểm tra xác nhận mật khẩu
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Tạo tài khoản mới
        Account account = new Account();
        account.setEmail(registrationRequest.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(registrationRequest.getPassword()));

        // Tạo thông tin người dùng mới
        User user = new User();
        user.setName(registrationRequest.getName());
        user.setPhoneNumber(registrationRequest.getPhoneNumber());
        user.setGender(registrationRequest.getGender());
        user.setBirthday(registrationRequest.getBirthday());
        user.setDateCreate(new Date());
        user.setStatus("true");

        // Thiết lập roleId thành 1
        Role defaultRole = roleRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
        user.setRole(defaultRole);

        // Liên kết tài khoản và người dùng
        user.setAccount(account);
        account.setUser(user);

        // Lưu thông tin người dùng trước
        userRepository.save(user);

        // Sau đó lưu tài khoản
        accountRepository.save(account);

        return Optional.of(user);
    }
}


