package app.timepiece.controller;

import app.timepiece.dto.UpdateUserDTO;
import app.timepiece.dto.UserDTO;
import app.timepiece.entity.User;
import app.timepiece.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping(value = "/{id}/UpdateUserProfile", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateUserById(@PathVariable Long id, @Valid @ModelAttribute UpdateUserDTO updateUserDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors:\n" + errors);
        }

        try {
            UserDTO userDTO = userService.updateUserById(id, updateUserDTO);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PreAuthorize("hasRole('Admin')")
    @PutMapping("admin/changeStatus/{userId}/status")
    public ResponseEntity<String> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam String status) {

        Boolean updated = userService.updateUserStatus(userId, status);
        if (updated) {
            return ResponseEntity.ok("Update status successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with id: " + userId);
        }
    }

}