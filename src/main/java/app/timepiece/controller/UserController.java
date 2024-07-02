package app.timepiece.controller;

import app.timepiece.dto.UpdateUserDTO;
import app.timepiece.dto.UserDTO;
import app.timepiece.entity.User;
import app.timepiece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}/UpdateUserProfile")
    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        UserDTO userDTO = userService.updateUserById(id, updateUserDTO);
        return ResponseEntity.ok(userDTO);
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