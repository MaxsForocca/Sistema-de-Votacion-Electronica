package com.sistema.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class SecurityConfig {
    @Bean    
    public PasswordEncoder PasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
