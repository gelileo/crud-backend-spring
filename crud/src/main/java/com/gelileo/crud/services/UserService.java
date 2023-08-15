package com.gelileo.crud.services;

import com.gelileo.crud.dto.PasswordDTO;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.exceptions.UserError;
import com.gelileo.crud.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final SystemUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SystemUser findUserByEmail(String email) {
        Optional<SystemUser> ret = userRepository.findByEmail(email);
        if (ret.isPresent()) {
            return ret.get();
        } else {
            throw UserError.NotFound.exception();
        }
    }

    public boolean validateOldPassword(SystemUser user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public void changePassword(SystemUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
