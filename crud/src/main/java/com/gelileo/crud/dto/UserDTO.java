package com.gelileo.crud.dto;

//@Data
//@AllArgsConstructor
public record UserDTO(
    String firstName,
    String lastName,
    String gender,
    String email
) {}
