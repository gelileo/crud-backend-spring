package com.gelileo.crud.repository;

import ch.qos.logback.core.model.INamedModel;
import com.gelileo.crud.entities.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    Optional<Privilege> findByName(String name);
}
