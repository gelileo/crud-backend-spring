package com.gelileo.crud.model;

import com.gelileo.crud.validation.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    @ValidPassword
    private String password;
}
