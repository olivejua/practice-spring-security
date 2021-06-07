package com.olivejua.practicespringsecurity.config.auth;

import com.olivejua.practicespringsecurity.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/static/**", "/h2-console/**", "/resource/photo_upload/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers("/post").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .oauth2Login()
//                        .defaultSuccessUrl("/user/processLogin")
//                        .loginProcessingUrl()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);
    }
}