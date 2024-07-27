package app.timepiece.service.serviceImpl;

import app.timepiece.dto.*;
import app.timepiece.entity.Account;
import app.timepiece.entity.Role;
import app.timepiece.entity.User;
import app.timepiece.entity.Wallet;
import app.timepiece.repository.AccountRepository;
import app.timepiece.repository.RoleRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.repository.WalletRepository;
import app.timepiece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        // Tạo ví mới cho người dùng
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(0.0);
        walletRepository.save(wallet);

        return Optional.of(user);
    }

    @Override
    public Optional<User> findByAccountEmail(String email) {
        return userRepository.findByAccountEmail(email);
    }

    @Override
    public String findEmailByUserId(Long userId) {
        return userRepository.findEmailByUserId(userId);
    }

    @Override
    public UserDTO getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return convertToDTO(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getAccount().getEmail());
        userDTO.setAddress(user.getAddress());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setStatus(user.getStatus());
        userDTO.setDateCreate(user.getDateCreate());
        userDTO.setGender(user.getGender());
        userDTO.setBirthday(user.getBirthday());
        userDTO.setCitizenID(user.getCitizenID());
        return userDTO;
    }

    @Override
    public UserDTO updateUserById(Long id, UpdateUserDTO updateUserDTO) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            updateUserEntity(user, updateUserDTO);
            userRepository.save(user);
            return convertToDTO(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    private void updateUserEntity(User user, UpdateUserDTO updateUserDTO) {
        if (updateUserDTO.getName() != null) {
            user.setName(updateUserDTO.getName());
        }
        if (updateUserDTO.getAddress() != null) {
            user.setAddress(updateUserDTO.getAddress());
        }
        if (updateUserDTO.getAvatar() != null) {
            String avatarUrl = uploadImage(updateUserDTO.getAvatar());
            user.setAvatar(avatarUrl);
        }
        if (updateUserDTO.getPhoneNumber() != null) {
            user.setPhoneNumber(updateUserDTO.getPhoneNumber());
        }
        if (updateUserDTO.getStatus() != null) {
            user.setStatus(updateUserDTO.getStatus());
        }
        if (updateUserDTO.getGender() != null) {
            user.setGender(updateUserDTO.getGender());
        }
        if (updateUserDTO.getBirthday() != null) {
            user.setBirthday(updateUserDTO.getBirthday());
        }
        if (updateUserDTO.getCitizenID() != null) {
            user.setCitizenID(updateUserDTO.getCitizenID());
        }
    }

    private String uploadImage(MultipartFile imageFile) {
        try {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(imageFile);
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    @Transactional
    public Boolean updateUserStatus(Long userId, String status) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(status);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
    @Override
    @Transactional
    public Optional<User> CreateUserByAdmin(CreateUserDTO createUser) {
        if (accountRepository.existsByEmail(createUser.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        if (!createUser.getPassword().equals(createUser.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Account account = new Account();
        account.setEmail(createUser.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(createUser.getPassword()));

        User user = User.builder()
                .name(createUser.getName())
                .avatar(createUser.getAvatar())
                .phoneNumber(createUser.getPhoneNumber())
                .dateCreate(new Date())
                .gender(createUser.getGender())
                .birthday(createUser.getBirthday())
                .account(account)
                .build();

        Role defaultRole = roleRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Default role not found"));
        user.setRole(defaultRole);

        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);

        return Optional.of(user);
    }

    @Override
    @Transactional
    public void changePassword(PasswordChangeDTO passwordChangeDTO) throws Exception {
        User user = userRepository.findById(passwordChangeDTO.getUserId())
                .orElseThrow(() -> new Exception("User not found"));

        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(), user.getAccount().getPassword())) {
            throw new Exception("Old password is incorrect");
        }

        if (!passwordChangeDTO.getNewPassword().equals(passwordChangeDTO.getConfirmNewPassword())) {
            throw new Exception("New password and confirm new password do not match");
        }

        user.getAccount().setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);
    }
}
