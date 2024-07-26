package app.timepiece.service;

import app.timepiece.dto.*;
import app.timepiece.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> registerUser(RegistrationRequestDTO registrationRequest);
    Optional<User> findByAccountEmail(String email);
    String findEmailByUserId(Long userId);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUserById(Long id, UpdateUserDTO updateUserDTO);
    Boolean updateUserStatus(Long userId, String status);
    Optional<User> CreateUserByAdmin(CreateUserDTO createUser);
    void changePassword(PasswordChangeDTO passwordChangeDTO) throws Exception;
}
