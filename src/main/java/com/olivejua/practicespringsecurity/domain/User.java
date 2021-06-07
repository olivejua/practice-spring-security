package com.olivejua.practicespringsecurity.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String socialCode;

    public static User createUser(String name, String email, Role role, String socialCode) {
        User newUser = new User();

        newUser.name = name;
        newUser.email = email;
        newUser.role = role;
        newUser.socialCode = socialCode;

        return newUser;
    }
}
