package app.timepiece.controller;

import app.timepiece.dto.UpdateUserDTO;
import app.timepiece.dto.UserDTO;
import app.timepiece.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}