package app.timepiece.service;

import app.timepiece.dto.CreateUserDTO;
import app.timepiece.dto.RegistrationRequestDTO;
import app.timepiece.dto.UpdateUserDTO;
import app.timepiece.dto.UserDTO;
import app.timepiece.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> registerUser(RegistrationRequestDTO registrationRequest);
    Optional<User> findByAccountEmail(String email);
    String findEmailByUserId(Long userId);
    UserDTO getUserById(Long id);
    UserDTO updateUserById(Long id, UpdateUserDTO updateUserDTO);
    Boolean updateUserStatus(Long userId, String status);
    Optional<User> CreateUserByAdmin(CreateUserDTO createUser);
}
