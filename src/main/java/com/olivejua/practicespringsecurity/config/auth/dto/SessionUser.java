package com.olivejua.practicespringsecurity.config.auth.dto;

import com.olivejua.practicespringsecurity.domain.Role;
import com.olivejua.practicespringsecurity.domain.User;

public class SessionUser {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String socialCode;

    public SessionUser(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.socialCode = user.getSocialCode();
    }

    public User toEntity() {
        return User.createUser(
                name,
                email,
                role,
                socialCode
        );
    }
}
