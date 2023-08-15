package com.gelileo.crud.controllers;

import com.gelileo.crud.model.RoleRequest;
import com.gelileo.crud.dto.UserDTO;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.repository.RoleRepository;
import com.gelileo.crud.repository.SystemUserRepository;
import com.gelileo.crud.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final SystemUserRepository userRepository;
    private final RoleService roleService;


    @PutMapping("/setRole/{userId}")
    public UserDTO updateRole(
            @PathVariable("userId") Long userId,
            @RequestBody RoleRequest req) {
        Optional<SystemUser> res = userRepository.findById(userId);
        if (res.isPresent()) {
            SystemUser existing = res.get();
            if (!existing.getUsername().equals(req.username())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user names don't match");
            }


            existing.setRoles(roleService.getRolesWithNames(req.roles()));
            userRepository.save(existing);
            return new UserDTO(existing);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Found no user with id: " + userId);
        }
    }

}
