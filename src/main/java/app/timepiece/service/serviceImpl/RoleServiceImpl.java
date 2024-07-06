package app.timepiece.service.serviceImpl;


import app.timepiece.dto.RoleDTO;
import app.timepiece.entity.Role;
import app.timepiece.repository.RoleRepository;
import app.timepiece.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDTO> getRoleById(Long id) {
        return roleRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public RoleDTO createRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }

    @Override
    public RoleDTO updateRole(Long id, String roleName) {
        Optional<Role> existingRole = roleRepository.findById(id);
        if (existingRole.isPresent()) {
            Role updatedRole = existingRole.get();
            updatedRole.setRoleName(roleName);
            return convertToDTO(roleRepository.save(updatedRole));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role with id " + id + " not found");
        }
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getRoleName());
    }
}