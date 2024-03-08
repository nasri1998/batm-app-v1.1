package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationSecurityConfig {
    // authentication
    @Bean  // dependensi injection untuk menjalankan proses secara background
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    // authorization
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests((auth) -> {
                try {
                    auth
                        .antMatchers("/api/regions").hasAuthority("Manager")
                        .anyRequest().permitAll()
                        // .antMatchers("/").permitAll()
                        // .antMatchers("/dummy").permitAll()
                        // .antMatchers("/user/**").permitAll()
                        // .antMatchers("/assets/**").permitAll()
                        // .anyRequest().authenticated()
                        // .antMatchers("/bot/**").authenticated()
                        // .antMatchers("/category/**").authenticated()
                        // .anyRequest().permitAll()
                        .and()
                        .httpBasic()
                        .and()
                        .logout();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return http.build();
    }

    // encrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
