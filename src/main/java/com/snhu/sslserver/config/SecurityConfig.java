package com.snhu.sslserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration.
 * Requires HTTPS for all requests and enables HTTP Basic authentication.
 * A production deployment should replace Basic auth with OAuth2 / JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .requiresChannel(channel -> channel
                .anyRequest().requiresSecure())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated())
            .httpBasic(basic -> {});
        return http.build();
    }
}
