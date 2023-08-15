package com.gelileo.crud.repository;

import com.gelileo.crud.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    Optional<Collection<Role>> findAllByNameIn(Collection<String> names);
}
