package com.gelileo.crud.services;

import com.gelileo.crud.entities.Role;
import com.gelileo.crud.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public Collection<Role> getRolesWithNames(Collection<String> names) {
        Optional<Collection<Role>> result = roleRepository.findAllByNameIn(names);
        if (result.isPresent()) {
            return result.get();
        } else {
            return Collections.emptyList();
        }
    }

}
