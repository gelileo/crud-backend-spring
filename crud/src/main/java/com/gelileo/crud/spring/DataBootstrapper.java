package com.gelileo.crud.spring;

import com.gelileo.crud.entities.Privilege;
import com.gelileo.crud.entities.Role;
import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.repository.PrivilegeRepository;
import com.gelileo.crud.repository.RoleRepository;
import com.gelileo.crud.repository.SystemUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataBootstrapper implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final SystemUserRepository userRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) {
            return;
        }
        final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        final Privilege passwordPrivilege = createPrivilegeIfNotFound("CHANGE_PASSWORD_PRIVILEGE");
        final Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        final List<Privilege> adminPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege, deletePrivilege));
        final List<Privilege> staffPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege, passwordPrivilege));
        final List<Privilege> userPrivileges = new ArrayList<>(Arrays.asList(readPrivilege, writePrivilege));
        final Role adminRole = createRoleIfNotFound("ADMIN", adminPrivileges);
        createRoleIfNotFound("STAFF", staffPrivileges);
        createRoleIfNotFound("USER", userPrivileges);

        // create initial user
        createUserIfNotFound("admin@gmail.com",
                "Admin",
                "User",
                SystemUser.Gender.MALE,
                "password",
                new ArrayList<>(Arrays.asList(adminRole)));

        alreadySetup = true;
    }

    @Transactional
    public Privilege createPrivilegeIfNotFound(final String name) {
        Optional<Privilege> result = privilegeRepository.findByName(name);
        if (result.isEmpty()) {
            Privilege privilege = new Privilege(name);
            privilege = privilegeRepository.save(privilege);
            return privilege;
        }
        return result.get();
    }

    @Transactional
    public Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Optional<Role> result = roleRepository.findByName(name);
        Role role = null;
        if (result.isEmpty()) {
            role = new Role(name);
        } else {
            role = result.get();
        }
        role.setPrivileges(privileges);
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    public SystemUser createUserIfNotFound(final String email,
                                           final String firstName,
                                           final String lastName,
                                           final SystemUser.Gender gender,
                                           final String password,
                                           final Collection<Role> roles) {
        SystemUser user = null;
        Optional<SystemUser> result = userRepository.findByEmail(email);
        if (result.isEmpty()) {
            user = SystemUser.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .gender(gender)
                    .password(passwordEncoder.encode(password))
                    .build();
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return user;
    }
}
