package com.gelileo.crud.controllers;

import com.gelileo.crud.dto.UserDTO;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final SystemUserRepository userRepository;
    @GetMapping("/{userId}")
    public UserDTO findUser(@PathVariable("userId") Long userId) {
        try {
            Optional<SystemUser> res = userRepository.findById(userId);
            if (res.isPresent()) {
                SystemUser user = res.get();
                return getUserDTO(user);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Found no user with id: " + userId);
        }
        return null;
     }

    private static UserDTO getUserDTO(SystemUser user) {
        return new UserDTO(user.getFirstName(),
                user.getLastName(),
                (user.getGender() == null ? SystemUser.Gender.UNDISCLOSED : user.getGender()).getName(),
                user.getEmail());
    }

    @GetMapping("")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserDTO> findAll() {
        try {
            List<SystemUser> results = userRepository.findAll();
            return results.stream().map(user -> {
                return getUserDTO(user);
            }).toList();
        } catch (Exception ex) {
            throw new RuntimeException();
        }

    }

    @GetMapping("/findByEmail")
    public UserDTO findByEmails(
            @RequestParam("username") String email) {
        SystemUser user = userRepository
                .findByEmail(email)
                .orElseThrow();
        return getUserDTO(user);
    }
    @PostMapping("")
    public Long addUser(
            @RequestBody UserDTO user) {

        SystemUser systemUser = SystemUser.builder()
                .lastName(user.lastName())
                .firstName(user.firstName())
                .email(user.email())
                .gender(SystemUser.Gender.fromName(user.gender()))
                .build();

        userRepository.save(systemUser);
        return systemUser.getId();
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UserDTO user) {
        Optional<SystemUser> res = userRepository.findById(userId);
        if (res.isPresent()) {
            SystemUser existing = res.get();
            if (user.firstName() != null) {
                existing.setFirstName(user.firstName());
            }

            if (user.lastName() != null) {
                existing.setLastName(user.lastName());
            }

            if (user.email() != null) {
                existing.setEmail(user.email());
            }

            if (user.gender() != null) {
                existing.setGender(SystemUser.Gender.fromName(user.gender()));
            }

            userRepository.save(existing);

            return getUserDTO(existing);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Found no user with id: " + userId);
        }
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        Optional<SystemUser> res = userRepository.findById(userId);
        if (res.isPresent()) {
            userRepository.delete(res.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Found no user with id: " + userId);
        }
    }
}
