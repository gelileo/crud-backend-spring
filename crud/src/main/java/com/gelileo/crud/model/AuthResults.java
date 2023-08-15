package com.gelileo.crud.model;

import com.gelileo.crud.entities.SystemUser;
import com.gelileo.crud.entities.Token;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResults {
    private Token accessToken;
    private Token refreshToken;
    private SystemUser user;
}
