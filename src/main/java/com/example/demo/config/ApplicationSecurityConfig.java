package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationSecurityConfig {
    @Bean // termasuk dependecies injection menjalankan
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // white listing = adalah akses yang boleh masuk
    // black listing = adalah akses yang tidak boleh masuk atau inputan
    // .authenticated = sembarang akses atau yang login saja bisa akses
    // permit all = bisa akses tanpa login
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    try {
                        auth
                                .antMatchers("/api/regions").authenticated()
                                .antMatchers(HttpMethod.GET, "/api/region/{id}").authenticated()
                                .antMatchers(HttpMethod.POST, "/api/region/**").hasAnyRole("admin")
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
