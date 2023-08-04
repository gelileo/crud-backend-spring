package com.gelileo.crud.entities;

import com.gelileo.crud.constants.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SystemUser implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName, lastName;
    private String password;
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())) // 'ROLE_' is the default prefix
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Gender {
        MALE("male"),
        FEMALE("female"),
        UNDISCLOSED("unknown");

        private final String name;

        Gender(String name) {
            this.name = name;
        }

        public String getName() { return name;}

        public static Gender fromName(String name) {
            for(Gender gender: Gender.values()) {
                if (gender.getName().equals(name.toLowerCase())) {
                    return gender;
                }
            }
            throw new IllegalArgumentException("No gender found with name: "+ name);
        }
    }

    private Gender gender;
    private String email;
}
