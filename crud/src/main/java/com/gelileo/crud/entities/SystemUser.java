package com.gelileo.crud.entities;

import jakarta.persistence.*;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SystemUser {
    @Id
    @GeneratedValue
    private Long id;
    private String username, firstName, lastName;
    private String password;

    public enum Gender {
        MALE("male"),
        FEMALE("female"),
        UNDISCLOSED("unknown");

        private final String name;
        private Gender(String name) {
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
