package com.gelileo.crud.dto;

import com.gelileo.crud.constants.Role;
import com.gelileo.crud.entities.SystemUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private Set<Role> roles;

    public UserDTO(SystemUser user) {
        super();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = (user.getGender() == null ? SystemUser.Gender.UNDISCLOSED : user.getGender()).getName();
        this.roles = user.getRoles();
    }

}
