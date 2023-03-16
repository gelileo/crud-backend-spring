package com.gelileo.crud.controllers;

import com.gelileo.crud.dto.UserDAO;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {
    private final SystemUserRepository userRepository;
    @GetMapping("/{userId}")
    public UserDAO findUser(@PathVariable("userId") Long userId) {
        try {
            Optional<SystemUser> res = userRepository.findById(userId);
            if (res.isPresent()) {
                SystemUser user = res.get();
                return getUserDAO(user);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Found no user with id: " + userId);
        }
        return null;
     }

    private static UserDAO getUserDAO(SystemUser user) {
        return new UserDAO(user.getFirstName(),
                user.getLastName(),
                user.getGender().getName(),
                user.getEmail());
    }

    @GetMapping("")
    public List<UserDAO> findAll() {
        List<SystemUser> results = userRepository.findAll();
        return results.stream().map(user -> {
            return getUserDAO(user);
        }).toList();

    }
    @PostMapping("")
    public Long addUser(
            @RequestBody UserDAO user) {

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
    public UserDAO updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UserDAO user) {
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

            return getUserDAO(existing);
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
