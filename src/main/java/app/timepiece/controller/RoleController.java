package app.timepiece.controller;

import app.timepiece.dto.RoleDTO;
import app.timepiece.entity.Role;
import app.timepiece.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        Optional<RoleDTO> role = roleService.getRoleById(id);
        if (role.isPresent()) {
            return ResponseEntity.ok(role.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public RoleDTO createRole(@RequestBody String roleName) {
        return roleService.createRole(roleName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @RequestBody String roleName) {
        try {
            RoleDTO updatedRole = roleService.updateRole(id, roleName);
            return ResponseEntity.ok(updatedRole);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(Integer.parseInt(e.getMessage())).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete success");
    }


}
