package com.olivejua.practicespringsecurity.config.auth.dto;

import com.olivejua.practicespringsecurity.domain.Role;
import com.olivejua.practicespringsecurity.domain.User;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String nickname;
    private String socialCode;

    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String socialCode) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.socialCode = socialCode;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver(registrationId, "id", attributes);
        }

        return ofGoogle(registrationId, userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return new OAuthAttributes(
                attributes,
                userNameAttributeName,
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                registrationId);
    }

    private static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return new OAuthAttributes(
                response,
                userNameAttributeName,
                (String) response.get("name"),
                (String) response.get("email"),
                registrationId);
    }

    public User toEntity() {
        return User.createUser(
                name,
                email,
                Role.GUEST,
                socialCode
        );
    }
}
