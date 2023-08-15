package com.gelileo.crud.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@EntityListeners(AuditingEntityListener.class)
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        scope = SystemUser.class,
//        property = "id"
//)
//@JsonIdentityReference(alwaysAsId = true)
public class SystemUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName, lastName;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    /*
    For fine-grained privileges, we use both Roles and Privileges.
    Regardless the terminology, in Spring they're all referred as
    granted authorities.
    Just note for roles to be able to work with Spring Web Expressions,
    they need to be prefixed with 'ROLE_'
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        for (final Role role : roles) {
            privileges.add("ROLE_" + role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
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
    private boolean archived = false;
//
//
//    @CreatedDate
//    @Column(nullable = false, updatable = false)
//    private OffsetDateTime created;
//
//    @LastModifiedDate
//    @Column(nullable = false)
//    private OffsetDateTime lastUpdated;
}
