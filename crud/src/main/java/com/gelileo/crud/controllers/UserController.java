package com.gelileo.crud.controllers;

import com.gelileo.crud.dto.UserDTO;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        Optional<SystemUser> res = userRepository.findById(userId);
        return res.map(UserController::getUserDTO)
                .orElseThrow(() -> {
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Found no user with id: " + userId);
                }
                        );
    }

    public static UserDTO getUserDTO(SystemUser user) {
        return UserDTO.builder()
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .gender((user.getGender() == null ? SystemUser.Gender.UNDISCLOSED : user.getGender()).getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    @GetMapping("")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UserDTO> findAll() {
        try {
            List<SystemUser> results = userRepository.findAll();
            return results.stream().map(UserController::getUserDTO).toList();
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
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .gender(SystemUser.Gender.fromName(user.getFirstName()))
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
            if (user.getFirstName() != null) {
                existing.setFirstName(user.getFirstName());
            }

            if (user.getLastName() != null) {
                existing.setLastName(user.getLastName());
            }

            if (user.getEmail() != null) {
                existing.setEmail(user.getEmail());
            }

            if (user.getGender() != null) {
                existing.setGender(SystemUser.Gender.fromName(user.getGender()));
            }
// This should be an admin endpoint.
//            if (user.getRoles() != null) {
//                existing.setRoles(user.getRoles());
//            }
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
