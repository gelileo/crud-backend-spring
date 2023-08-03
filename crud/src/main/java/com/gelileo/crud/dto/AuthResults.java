package com.gelileo.crud.dto;

import com.gelileo.crud.entities.Token;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResults {
    private Token accessToken;
    private Token refreshToken;
}
