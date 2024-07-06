package app.timepiece.service;

import app.timepiece.dto.RoleDTO;
import app.timepiece.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<RoleDTO> getAllRoles();
    Optional<RoleDTO> getRoleById(Long id);
    RoleDTO createRole(String roleName);
    RoleDTO updateRole(Long id, String roleName);
    void deleteRole(Long id);
}
