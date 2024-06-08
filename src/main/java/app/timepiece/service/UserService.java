package app.timepiece.service;

import app.timepiece.dto.RegistrationRequestDTO;
import app.timepiece.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> registerUser(RegistrationRequestDTO registrationRequest);
}
