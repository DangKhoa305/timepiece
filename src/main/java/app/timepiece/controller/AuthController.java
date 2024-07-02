package app.timepiece.controller;

import app.timepiece.dto.ErrorResponse;
import app.timepiece.dto.JwtAuthenticationResponse;
import app.timepiece.dto.LoginRequestDTO;
import app.timepiece.dto.RegistrationRequestDTO;
import app.timepiece.entity.User;
import app.timepiece.security.JwtTokenProvider;
import app.timepiece.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String email = loginRequest.getEmail();
            Optional<User> userOptional = userService.findByAccountEmail(email);
            if (!userOptional.isPresent()) {
                throw new UsernameNotFoundException("User not found with email: " + email);
            }
            User user = userOptional.get();

            if (!user.getStatus().equalsIgnoreCase("true")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Account is banned"));
            }

            String jwt = jwtTokenProvider.generateToken(user.getId());
            JwtAuthenticationResponse response = new JwtAuthenticationResponse(jwt, user.getName(), user.getRole().getRoleName());
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid email or password"));

        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequestDTO registrationRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors:\n" + errors);
        }
        try {
            userService.registerUser(registrationRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}