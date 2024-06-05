package app.timepiece.service;

import app.timepiece.dto.RegistrationRequestDTO;
import app.timepiece.entity.User;

import java.util.Optional;

public interface AccountService {
    Optional<User> loginUser(String email, String password);
    Optional<User> registerUser(RegistrationRequestDTO registrationRequest);
}
