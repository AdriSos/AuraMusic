package com.auramusic.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Permite enviar datos desde nuestro HTML
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Deja pasar a todas las rutas
                )
                .formLogin(form -> form.disable())   // APAGA la pantalla blanca de login de Spring
                .httpBasic(basic -> basic.disable()); // APAGA las ventanas emergentes de contraseña

        return http.build();
    }
}