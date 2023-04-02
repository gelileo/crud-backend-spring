package com.gelileo.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;;

//@Data
//@AllArgsConstructor
public record UserDAO (
    String firstName,
    String lastName,
    String gender,
    String email
) {}
