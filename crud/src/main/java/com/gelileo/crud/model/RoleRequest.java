package com.gelileo.crud.model;


import com.gelileo.crud.entities.Role;

import java.util.Set;

public record RoleRequest(String username, Set<String> roles) {
}
