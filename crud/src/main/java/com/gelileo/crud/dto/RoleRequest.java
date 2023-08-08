package com.gelileo.crud.dto;

import com.gelileo.crud.constants.Role;

import java.util.Set;

public record RoleRequest(String username, Set<Role> roles) {
}
